package com.example.training.controller;

import java.util.List;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.training.entity.Training;
import com.example.training.input.TrainingAdminInput;
import com.example.training.service.TrainingAdminService;

@Controller
@RequestMapping("/admin/training")
public class TrainingAdminController {

    private final TrainingAdminService trainingAdminService;

    public TrainingAdminController(TrainingAdminService trainingAdminService) {
        this.trainingAdminService = trainingAdminService;
    }

    @GetMapping("/display-list")
    public String displayList(Model model) {
        List<Training> trainings = trainingAdminService.findAll();
        model.addAttribute("trainingList", trainings);
        return "admin/training/trainingList";
    }

    @GetMapping("/display-update-form")
    public String displayUpdateForm(@RequestParam String trainingId, Model model) {
        Training training = trainingAdminService.findById(trainingId);
        TrainingAdminInput trainingAdminInput = new TrainingAdminInput();
        trainingAdminInput.setId(training.getId());
        trainingAdminInput.setTitle(training.getTitle());
        trainingAdminInput.setStartDateTime(training.getStartDateTime());
        trainingAdminInput.setEndDateTime(training.getEndDateTime());
        trainingAdminInput.setReserved(training.getReserved());
        trainingAdminInput.setCapacity(training.getCapacity());
        model.addAttribute("trainingAdminInput", trainingAdminInput);
        return "admin/training/updateForm";
    }

    @PostMapping(value = "/validate-update-input")
    public String validateUpdateInput(@Validated TrainingAdminInput trainingAdminInput, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/training/updateForm";
        }
        return "admin/training/updateConfirmation";
    }

    @PostMapping(value = "/update", params = "correct")
    public String correctUpdateInput(@Validated TrainingAdminInput trainingAdminInput , @AuthenticationPrincipal UserDetails userDetails) {
    	System.out.println("UserName" + userDetails.getUsername() + " Role" + userDetails.getAuthorities().toString());
    	return "admin/training/updateForm";
    }

    @PostMapping(value = "/update", params = "update")
    public String update(@Validated TrainingAdminInput trainingAdminInput, Authentication authentication) {
    	System.out.println("authentication UserName " + authentication.getName() );
        trainingAdminService.update(trainingAdminInput, authentication.getName());
        return "admin/training/updateCompletion";
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public String displayUpdateFailure() {
        return "admin/training/updateFailure";
    }

}
