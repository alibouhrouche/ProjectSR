package com.app;

import com.app.entities.Client;
import com.app.entities.Role;
import com.app.services.ClientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ProjectSrv1Application {
    
    public static void main(String[] args) {
        SpringApplication.run(ProjectSrv1Application.class, args);
    }



}