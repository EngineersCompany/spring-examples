package com.example.training;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.support.JdbcTransactionManager;

import com.example.training.entity.Reservation;
import com.example.training.input.ReservationInput;
import com.example.training.service.ReservationService;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

//@Configuration
//@ComponentScan
//@EnableTransactionManagement
@SpringBootApplication
public class TrainingApplication {
//    @Bean
//    public DataSource dataSource() {
//        EmbeddedDatabase dataSource = new EmbeddedDatabaseBuilder()
//                .addScripts("schema.sql", "data.sql")
//                .setType(EmbeddedDatabaseType.H2).build();
//        return dataSource;
//    }
//
//    @Bean
//    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }
//
//    @Bean
//    public JdbcTransactionManager transactionManager(DataSource dataSource) {
//        return new JdbcTransactionManager(dataSource);
//    }

    public static void main(String[] args) {
//        ApplicationContext context = new AnnotationConfigApplicationContext(TrainingApplication.class);
        ApplicationContext context = SpringApplication.run(TrainingApplication.class,args);
        // トランザクション制御のログを出力してくれるように設定
        ((Logger) LoggerFactory.getLogger(JdbcTransactionManager.class)).setLevel(Level.DEBUG);
        ReservationService reservationService = context.getBean(ReservationService.class);

        ReservationInput reservationInput = new ReservationInput();
        reservationInput.setName("東京太郎");
        reservationInput.setPhone("090-0000-0000");
        reservationInput.setEmailAddress("taro@example.com");
        reservationInput.setStudentTypeCode("FREELANCE");
        reservationInput.setTrainingId("t01");

        Reservation reservation = reservationService.reserve(reservationInput);

        System.out.println("予約の処理が完了しました。 予約ID=" + reservation.getId());
    }

}

