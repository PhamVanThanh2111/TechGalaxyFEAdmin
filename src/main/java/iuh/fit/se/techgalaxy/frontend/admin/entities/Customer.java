package iuh.fit.se.techgalaxy.frontend.admin.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import iuh.fit.se.techgalaxy.frontend.admin.entities.enumeration.CustomerStatus;
import iuh.fit.se.techgalaxy.frontend.admin.entities.enumeration.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private String id;
    private CustomerStatus userStatus;
    private Account account;
    private String name;
    private String phone;
    private Gender gender;
    private String avatar;
    private LocalDate dateOfBirth;
    private List<Order> orders;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;

}
