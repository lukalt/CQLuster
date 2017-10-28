package me.lukas81298.cqluster.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author lukas
 * @since 28.10.2017
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Group {

    private UUID uuid;
    private String name;
    private List<String> permissions = new ArrayList<>();

    public Group( UUID uuid ) {
        this.uuid = uuid;
    }

}
