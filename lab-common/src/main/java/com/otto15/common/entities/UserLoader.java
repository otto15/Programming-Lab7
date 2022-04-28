package com.otto15.common.entities;

import com.otto15.common.entities.validators.UserValidator;
import com.otto15.common.utils.DataNormalizer;

import java.util.Scanner;

public class UserLoader {

    public String loadLogin() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter login: ");
            String data = scanner.nextLine();
            try {
                String[] normalizeData = DataNormalizer.normalize(data);
                return UserValidator.getValidatedLogin(normalizeData);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public String loadPassword() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter password: ");
            String data = scanner.nextLine();
            try {
                String[] normalizeData = DataNormalizer.normalize(data);
                return UserValidator.getValidatedPassword(normalizeData);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public User loadUser() {
        return new User(loadLogin(), loadPassword());
    }

}
