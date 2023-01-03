package com.softserve.edu.models.placeholder.login;

import com.softserve.edu.models.BaseDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class LoginResponseDto extends BaseDto {

    private Integer id;
    private String email;
    private String roleName;
    private String accessToken;

    public Integer getId() {
        return this.id;
    }

    public LoginResponseDto setId(Integer id) {
        this.id = id;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public String getEmail() {
        return this.email;
    }

    public LoginResponseDto setEmail(String email) {
        this.email = email;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public LoginResponseDto setRoleName(String roleName) {
        this.roleName = roleName;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public LoginResponseDto setAccessToken(String accessToken) {
        this.accessToken = accessToken;
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
        LoginResponseDto loginResponseDto = (LoginResponseDto) o;
        return new EqualsBuilder()
                .append(id, loginResponseDto.id)
                .append(email, loginResponseDto.email)
                .append(roleName, loginResponseDto.roleName)
                .append(accessToken, loginResponseDto.accessToken)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(email)
                .append(roleName)
                .append(accessToken)
                .toHashCode();
    }

}
