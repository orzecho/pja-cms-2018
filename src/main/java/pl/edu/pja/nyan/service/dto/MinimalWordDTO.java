package pl.edu.pja.nyan.service.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MinimalWordDTO implements Serializable {

    private Long id;

    @NotNull
    private String translation;

    @NotNull
    private String kana;

    private String kanji;
}
