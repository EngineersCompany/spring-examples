package com.example.training.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.training.entity.Training;
import com.example.training.exception.TrainingIdAlreadyExistsException;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(Exception.class)
    public String systemError(Exception ex) {
        ex.printStackTrace();
        return "systemError";
    }
    // TrainingIdAlreadyExistsException 専用のハンドラ
    @ExceptionHandler(TrainingIdAlreadyExistsException.class)
    public String handleTrainingIdAlreadyExists(TrainingIdAlreadyExistsException ex, Model model) {
        model.addAttribute("duplicateError", ex.getMessage()); // エラーメッセージをModelに追加
        model.addAttribute("trainingAdminInput", new Training()); // フォームの再表示のために空のTrainingオブジェクトも渡す
        return "admin/training/registrationForm"; // 研修登録フォームのテンプレート名を返す
    }
}
