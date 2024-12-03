package com.literalura.literalura.model;
import jakarta.persistence.*;


@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long Id;

    @Column(unique = true)
    private String titulo;

    private String autor;
    private String idioma;
    private Double numeroDescargas;

    public Libro(){}

    public Libro(DatosLibro datosLibro){
        this.titulo= datosLibro.titulo();
        if(datosLibro.autor().isEmpty()){
            this.autor="No disponible";
        }else{
            this.autor= String.valueOf(datosLibro.autor().get(0).nombre());
        }
        this.idioma= datosLibro.idiomas().get(0);
        this.numeroDescargas= datosLibro.numeroDeDescargas();
    }
    public Libro(DatosLibro datosLibro, String idioma){
        this.titulo= datosLibro.titulo();
        if(datosLibro.autor().isEmpty()){
            this.autor="No disponible";
        }else{
            this.autor= String.valueOf(datosLibro.autor().get(0).nombre());
        }
        this.idioma= idioma;
        this.numeroDescargas= datosLibro.numeroDeDescargas();
    }

    @Override
    public String toString() {
        return "\n# Detalles de Libro: \n" +
                "Titulo: " + titulo + '\n' +
                "Autor: " + autor + '\n' +
                "Idioma: " + idioma + '\n' +
                "Número de Descargas: " + numeroDescargas + '\n' +'\n' ;
    }

    // Getter & Setters

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Double getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Double numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }
}
