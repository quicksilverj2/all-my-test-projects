package com.smu.stakeme.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * Created by jitheshrajan on 11/19/18.
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StakeUser {

    @Id
    @Getter @Setter
    private String id;

    @Getter @Setter
    @Indexed
    private String username;

    @Getter @Setter
    private String firstName;
    @Getter @Setter
    private String lastName;

    @Getter @Setter
    private String countryCode;
    @Getter @Setter
    private String phoneNumber;

    @Getter @Setter
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        StakeUser stakeUser = (StakeUser) o;

        if (!id.equals(stakeUser.id)) return false;
        if (!username.equals(stakeUser.username)) return false;
        if (!firstName.equals(stakeUser.firstName)) return false;
        if (lastName != null ? !lastName.equals(stakeUser.lastName) : stakeUser.lastName != null) return false;
        if (countryCode != null ? !countryCode.equals(stakeUser.countryCode) : stakeUser.countryCode != null)
            return false;
        if (!phoneNumber.equals(stakeUser.phoneNumber)) return false;
        return email.equals(stakeUser.email);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (countryCode != null ? countryCode.hashCode() : 0);
        result = 31 * result + phoneNumber.hashCode();
        result = 31 * result + email.hashCode();
        return result;
    }
}
