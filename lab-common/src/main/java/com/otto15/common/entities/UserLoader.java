package com.otto15.common.entities;

import com.otto15.common.entities.validators.UserValidator;
import com.otto15.common.exceptions.EndOfStreamException;
import com.otto15.common.io.DataReader;
import com.otto15.common.utils.DataNormalizer;

import java.io.IOException;
import java.io.InputStreamReader;

public class UserLoader {

    public String loadLogin() throws EndOfStreamException, IOException {
        DataReader reader = new DataReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("Enter login: ");
            String data = reader.inputLine();
            try {
                String[] normalizeData = DataNormalizer.normalize(data);
                return UserValidator.getValidatedLogin(normalizeData);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public String loadPassword() throws EndOfStreamException, IOException {
        DataReader reader = new DataReader(new java.io.InputStreamReader(System.in));
        while (true) {
            System.out.print("Enter password: ");
            String data = reader.inputLine();
            try {
                String[] normalizeData = DataNormalizer.normalize(data);
                return UserValidator.getValidatedPassword(normalizeData);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public User loadUser() throws EndOfStreamException, IOException {
        return new User(loadLogin(), loadPassword());
    }

}
