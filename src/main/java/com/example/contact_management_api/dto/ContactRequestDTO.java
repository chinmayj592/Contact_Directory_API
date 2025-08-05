

package com.example.contact_management_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactRequestDTO {
    private String name;
    private String phone;
    private String email;
    private String address;
}
