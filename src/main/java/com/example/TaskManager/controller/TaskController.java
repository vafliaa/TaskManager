package com.example.TaskManager.controller;

import com.example.TaskManager.Repository.GroupRepository;
import com.example.TaskManager.Repository.TaskRepository;
import com.example.TaskManager.Repository.UserRepository;
import com.example.TaskManager.model.Group;
import com.example.TaskManager.model.Task;
import com.example.TaskManager.model.User;
import com.example.TaskManager.service.TaskService;
import com.example.TaskManager.service.GroupService;
import com.example.TaskManager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

@RequiredArgsConstructor
@Controller
public class TaskController {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    private final TaskService taskService;

    private final GroupService groupService;

    private final UserService userService;

    private List<String> userGroups = new ArrayList<>(); // Список групп пользователя


    @GetMapping("/taskPage")
    public String showTaskPage(@RequestParam("groupId") Long groupId, Model model, HttpSession session) {
        String loggedInUsername = (String) session.getAttribute("loggedInUser");
        System.out.println("Logged In Username: " + loggedInUsername); // Проверяем, что получили имя пользователя
        System.out.println("Group ID: " + groupId); // Проверяем, что получили ID группы
        boolean isGroupOwner = groupService.isGroupOwner(loggedInUsername, groupId);
        System.out.println("Is Group Owner: " + isGroupOwner); // Проверяем результат проверки статуса владельца группы
        String groupCode = groupService.getGroupCodeById(groupId);

        String groupName = groupService.getGroupNameById(groupId);

        // Получаем список всех задач по groupId
        List<Task> tasks = taskService.getTasksByGroupId(groupId);
        // Создаем список для хранения имен исполнителей
        List<String> executorNames = new ArrayList<>();
        // Получаем список поручителей для каждой задачи в группе
        List<String> guarantorNames = new ArrayList<>();
        // Создаем список для хранения дат создания задач
        List<Date> taskCreatedAtList = new ArrayList<>();

        // Создаем списки для задач в разных статусах
        List<Task> todoTasks = new ArrayList<>();
        List<Task> inProgressTasks = new ArrayList<>();
        List<Task> completedTasks = new ArrayList<>();

        // Создаем список для хранения идентификаторов задач
        List<Long> taskId = new ArrayList<>();

        // Распределяем задачи по спискам в зависимости от их статуса
        for (Task task : tasks) {
            Long executorId = task.getExecutorId();
            String executorName = userService.getUsernameById(executorId);
            System.out.println(executorName);
            executorNames.add(executorName);

            Long guarantorId = task.getExecutorId();
            String guarantorName = userService.getUsernameById(guarantorId);
            guarantorNames.add(guarantorName);

            taskCreatedAtList.add(task.getCreatedDate());

            taskId.add(task.getId());

            // Логика по обработке других статусов, если необходимо
            if (task.getStatusId() == 1L) {
                todoTasks.add(task);
            } else if (task.getStatusId() == 2L) {
                inProgressTasks.add(task);
            } else if (task.getStatusId() == 3L) {
                completedTasks.add(task);
            }
        }

        // Добавляем списки задач в модель для отображения на странице
        model.addAttribute("todoTasks", todoTasks);
        model.addAttribute("inProgressTasks", inProgressTasks);
        model.addAttribute("completedTasks", completedTasks);
        model.addAttribute("executorNames", executorNames);
        model.addAttribute("guarantorNames", guarantorNames);
        model.addAttribute("taskCreatedAtList", taskCreatedAtList);
        model.addAttribute("taskId", taskId);

        // Добавляем остальные неизмененные атрибуты в модель
        model.addAttribute("groupName", groupName);
        model.addAttribute("isGroupOwner", isGroupOwner);
        model.addAttribute("groupMembers", isGroupOwner ? groupService.getGroupMembers(groupId) : new ArrayList<>());
        model.addAttribute("groupId", groupId);
        model.addAttribute("groupCode", groupCode);
        model.addAttribute("loggedInUsername", loggedInUsername);

        return "taskPage";
    }

    @PostMapping("/editGroupName")
    public String editGroupName(@RequestParam(value = "newGroupName", required = false) String newGroupName,
                                @RequestParam("groupId") Long groupId) {
        // Найдем группу по ее идентификатору
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if (optionalGroup.isPresent()) {
            Group group = optionalGroup.get();
            // Установим новое имя группы
            group.setName(newGroupName);
            // Сохраним изменения
            groupRepository.save(group);
        }

        return "redirect:/taskPage?groupId=" + groupId;
    }

    @PostMapping("/leaveGroup")
    public String leaveGroup(@RequestParam("groupId") Long groupId, HttpSession session) {
        String loggedInUsername = (String) session.getAttribute("loggedInUser");
        if (loggedInUsername != null) {
            User user = userRepository.findByUsername(loggedInUsername);
            if (user != null) {
                Group group = groupRepository.findById(groupId).orElse(null);
                if (group != null) {
                    // Проверяем, является ли пользователь участником группы
                    if (group.getMembers().contains(user)) {
                        if (group.getOwner().equals(user)) {
                            List<Task> tasks = taskRepository.findByGroupId(groupId);
                            taskRepository.deleteAll(tasks);
                            // Если пользователь является создателем группы, удаляем всю группу
                            groupRepository.delete(group);
                        } else {
                            // Если пользователь не является создателем группы, удаляем его из группы и связанные задачи
                            group.getMembers().remove(user);
                            // Получаем задачи, связанные с пользователем и удаляем их
                            List<Task> tasks = taskRepository.findByExecutorId(user.getId());
                            taskRepository.deleteAll(tasks);
                            groupRepository.save(group);
                        }
                        return "redirect:/"; // Перенаправляем на главную страницу
                    } else {
                        // Если пользователь не является участником группы
                        return "redirect:/error"; // Перенаправляем на страницу с сообщением об ошибке
                    }
                } else {
                    // Если группа не найдена
                    return "redirect:/error"; // Перенаправляем на страницу с сообщением об ошибке
                }
            } else {
                // Если пользователь не найден
                return "redirect:/error"; // Перенаправляем на страницу с сообщением об ошибке
            }
        }
        return "redirect:/";
    }


    // Закончить ошибка с заполнением полей guarantorId
    @PostMapping("/task/create")
    public String createTask(@RequestParam("assignee") String assigneeName,
                             @RequestParam("taskText") String taskText,
                             @RequestParam("deadline") @DateTimeFormat(pattern = "yyyy-MM-dd") Date deadline,
                             @RequestParam("groupId") Long groupId,
                             HttpSession session,
                             Model model) {
        // Получаем id пользователя по его имени (исполнитель)
        Long executorId = userService.findUserIdByUsername(assigneeName);
        if (executorId == null) {
            // Обработка случая, если пользователь (исполнитель) не найден
            return "errorPage";
        }

        // Получаем id пользователя по его идентификатору в сессии (гарант)
        String loggedInUsername = (String) session.getAttribute("loggedInUser");
        Long guarantorId = userService.findUserIdByUsername(loggedInUsername);
        if (guarantorId == null) {
            // Обработка случая, если пользователь (гарант) не найден
            return "errorPage";
        }

        // Создаем новую задачу
        Task task = new Task();
        task.setIdGroup(groupId);
        task.setGuarantorId(guarantorId);
        task.setExecutorId(executorId);
        task.setDescription(taskText);
        task.setStatusId(1L); // 1L соответствует статусу "Не начато"
        task.setDeadline(deadline);
        task.setCreatedDate(new Date());
        // Создаем задачу с помощью сервиса
        taskService.createTask(task);

        model.addAttribute("task", task);

        // Перенаправляем пользователя на страницу с задачами для текущей группы
        return "redirect:/taskPage?groupId=" + groupId;
    }




    @PostMapping("/task/start/{taskId}")
    public String startTask(@RequestParam("taskId") String taskIdString) {
        Long taskId = Long.parseLong(taskIdString);
        System.out.println("Received taskId: " + taskId);
        // Получаем задачу по ее идентификатору
        Task task = taskService.getTaskById(taskId);
        if (task == null) {
            // Обработка случая, если задача не найдена
            return "errorPage";
        }

        // Обновляем статус задачи на "В работе"
        task.setStatusId(2L); // Предположим, что 2L соответствует статусу "В работе"
        taskService.updateStatusTask(task);

        // Перенаправляем пользователя на страницу с задачами для текущей группы
        return "redirect:/taskPage?groupId=" + task.getIdGroup();
    }

    @PostMapping("/task/finish/{taskId}")
    public String finishTask(@RequestParam("taskId") String taskIdString) {
        Long taskId = Long.parseLong(taskIdString);
        // Получаем задачу по ее идентификатору
        Task task = taskService.getTaskById(taskId);
        if (task == null) {
            // Обработка случая, если задача не найдена
            return "errorPage";
        }

        // Устанавливаем время завершения задачи
        task.setFinishDate(new Date());

        task.setStatusId(3L);
        taskService.updateStatusTask(task);

        // Перенаправляем пользователя на страницу с задачами для текущей группы
        return "redirect:/taskPage?groupId=" + task.getIdGroup();
    }

    @PostMapping("/task/delete/{taskId}")
    public String deleteTask(@RequestParam("taskId") String taskIdString) {
        Long taskId = Long.parseLong(taskIdString);
        // Получаем задачу по ее идентификатору
        Task task = taskService.getTaskById(taskId);

        taskService.deleteTaskById(taskId);

        // Перенаправляем пользователя на страницу с задачами для текущей группы
        return "redirect:/taskPage?groupId=" + task.getIdGroup();
    }

    @PostMapping("/task/update/{taskId}")
    public String updateTask(@RequestParam("taskId") Long taskId,
                             @RequestParam("assignee") String assigneeName,
                             @RequestParam("taskText") String taskText,
                             @RequestParam("deadline") @DateTimeFormat(pattern = "yyyy-MM-dd") Date deadline,
                                        HttpSession session) {

        // Получаем задачу по ее идентификатору
        Task task = taskService.getTaskById(taskId);
        // Получаем id пользователя по его имени (исполнитель)
        Long executorId = userService.findUserIdByUsername(assigneeName);

        // Обновляем данные задачи
        task.setExecutorId(executorId);
        task.setDescription(taskText);
        task.setDeadline(deadline);

        // Обновляем задачу с помощью сервиса
        taskService.updateTask(taskId, task);

        return "redirect:/taskPage?groupId=" + task.getIdGroup();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////Из indexController////////////////////////////////////////////////////////////////////////////////////////////////

    @PostMapping("/createGroup")
    public String createGroup(@RequestParam(value = "groupName", required = false, defaultValue = "NoNameGroup") String groupName, HttpSession session) {
        String loggedInUsername = (String) session.getAttribute("loggedInUser");
        if (loggedInUsername != null) {
            User owner = userRepository.findByUsername(loggedInUsername);
            if (owner != null) {
                String groupCode = UUID.randomUUID().toString(); // Генерируем уникальный код для группы
                Group group = new Group(groupName, owner, groupCode);

                // Добавляем созданную группу в список групп пользователя
                owner.getGroups().add(group);

                // Добавляем владельца в участники группы
                group.getMembers().add(owner);

                // Сохраняем созданную группу
                groupRepository.save(group);

                // Сохраняем обновленного пользователя
                userRepository.save(owner);
            }
        }
        return "redirect:/";
    }

    @PostMapping("/joinGroup")
    public String joinGroup(@RequestParam("groupCode") String groupCode, HttpSession session) {
        String loggedInUsername = (String) session.getAttribute("loggedInUser");
        if (loggedInUsername != null) {
            User user = userRepository.findByUsername(loggedInUsername);
            Group group = groupRepository.findByCode(groupCode);
            if (group != null && user != null) {
                // Добавляем пользователя в группу
                group.getMembers().add(user);
                // Сначала сохраняем пользователя
                userRepository.save(user);
                // Затем сохраняем группу
                groupRepository.save(group);
            }
        }
        return "redirect:/";
    }

    @GetMapping("/taskPage")
    public String showTaskPage(Model model, HttpSession session) {
        String loggedInUsername = (String) session.getAttribute("loggedInUser");
        if (loggedInUsername != null) {
            System.out.println("Session ID: " + session.getId()); // Выведем ID сессии
            User loggedInUser = userRepository.findByUsername(loggedInUsername);
            if (loggedInUser != null) {
                // Проверяем наличие групп у пользователя
                List<Group> userGroups = loggedInUser.getGroups();
                if (userGroups != null && !userGroups.isEmpty()) {
                    model.addAttribute("groupList", userGroups);
                } else {
                    model.addAttribute("groupList", null); // Если групп нет, устанавливаем список групп как null
                }
            }
        }
        return "index";
    }
}