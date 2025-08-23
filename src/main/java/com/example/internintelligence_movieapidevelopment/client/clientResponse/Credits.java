package com.example.internintelligence_movieapidevelopment.client.clientResponse;

import com.example.internintelligence_movieapidevelopment.dto.response.PersonResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Credits {
    List<PersonResponseDto> cast;
}
