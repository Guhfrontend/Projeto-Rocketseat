package br.com.gustavo.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/tasks")
public class TaskController {

  @Autowired
  private TaskRepository taskRepository;

  @PostMapping("/")


  public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
    System.out.println("Chegou no controler" + request.getAttribute("idUser")); 
    var idUser = request.getAttribute("idUser");
    taskModel.setIdUser((UUID) idUser);

    var currentDate = LocalDateTime.now();
    if(currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEntAt())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("A data de inicio ou de termino deve ser maior do que a data atual");
    }
    if(taskModel.getStartAt().isAfter(taskModel.getEntAt())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("A data de inicio deve ser menor que a data de termino");
    }


      var task = this.taskRepository.save(taskModel);
     return ResponseEntity.status(HttpStatus.OK).body(task);
}

@GetMapping("/")
public List<TaskModel> list(HttpServletRequest request){
  var idUser = request.getAttribute("idUser");
  var tasks = this.taskRepository.findByIdUser((UUID) idUser) ;
  return tasks;
}


}