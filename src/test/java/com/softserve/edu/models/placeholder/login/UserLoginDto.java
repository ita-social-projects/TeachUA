package com.softserve.edu.models.placeholder.login;

import com.softserve.edu.models.BaseDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class UserLoginDto extends BaseDto {

    private String email;
    private String password;

    public String getEmail() {
        return this.email;
    }

    public UserLoginDto setEmail(String email) {
        this.email = email;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public String getPassword() {
        return this.password;
    }

    public UserLoginDto setPassword(String password) {
        this.password = password;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserLoginDto userLoginDto = (UserLoginDto) o;
        return new EqualsBuilder()
                .append(email, userLoginDto.email)
                .append(password, userLoginDto.password)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(email)
                .append(password)
                .toHashCode();
    }

}
