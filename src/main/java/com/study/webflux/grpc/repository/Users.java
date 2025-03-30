package com.study.webflux.grpc.repository;

import com.study.webflux.grpc.service.User;
import com.study.webflux.util.ProtoTimeStampMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table("users")
public class Users {

    @Id
    private Long id;
    private String name;
    private String email;

    @Column("created_at")
    private Instant createdAt;

    public User toProto() {
        return User.newBuilder()
                   .setId(getId())
                   .setName(getName())
                   .setEmail(getEmail())
                   .setCreatedAt(ProtoTimeStampMapper.fromInstant(getCreatedAt()))
                   .build();
    }
}
