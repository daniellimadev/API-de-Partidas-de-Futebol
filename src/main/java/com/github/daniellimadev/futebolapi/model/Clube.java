package com.github.daniellimadev.futebolapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
public class Clube {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2)
    private String nome;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{2}$", message = "Sigla do estado deve ter 2 letras maiúsculas.")
    private String estado;

    @PastOrPresent
    private LocalDate dataCriacao;

    private boolean ativo;

    // Construtor
    public Clube(Long id, String nome, String estado, boolean ativo, LocalDate dataCriacao) {
        this.id = id;
        this.nome = nome;
        this.estado = estado;
        this.ativo = ativo;
        this.dataCriacao = dataCriacao;
    }

    public Clube() {

    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank @Size(min = 2) String getNome() {
        return nome;
    }

    public void setNome(@NotBlank @Size(min = 2) String nome) {
        this.nome = nome;
    }

    public @NotBlank @Pattern(regexp = "^[A-Z]{2}$", message = "Sigla do estado deve ter 2 letras maiúsculas.") String getEstado() {
        return estado;
    }

    public void setEstado(@NotBlank @Pattern(regexp = "^[A-Z]{2}$", message = "Sigla do estado deve ter 2 letras maiúsculas.") String estado) {
        this.estado = estado;
    }

    public @PastOrPresent LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(@PastOrPresent LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}