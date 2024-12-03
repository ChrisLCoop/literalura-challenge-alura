package com.literalura.literalura.principal;

import com.literalura.literalura.model.*;
import com.literalura.literalura.repository.AutorRepository;
import com.literalura.literalura.repository.LibroRepository;
import com.literalura.literalura.service.ConsumoAPI;
import com.literalura.literalura.service.ConvierteDatos;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    Scanner teclado = new Scanner(System.in);
    private final String URL_BASE = "https://gutendex.com/books/";
    private List<Libro> libros;
    private List<Autor> autores;
    int cantidadAutores= 0;
    int cantidadLibros= 0;


    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();

    private LibroRepository repositorio;
    private AutorRepository autorRepositorio;

    public Principal(LibroRepository repository, AutorRepository autorRepository) {
        this.repositorio = repository;
        this.autorRepositorio = autorRepository;
    }

    public void showMenu(){

        System.out.println("Buscador de libros WorldBook");
        var option = -1;


        try{
            while(option != 0){

                System.out.println("""
                
                Seleccione una opción:
               
                1 - Buscar y registrar Libro por Título
                2 - Buscar y registrar los Top 10 por Idioma
                3 - Listar Libros registrados
                4 - Listar Libros registrados por Idioma
                5 - Listar Libros resgistrados por Autor
                6 - Listar Autores registrados
                7 - Listar Autores vivos en un determinado año
                
                0- Salir
                
                """);
                Scanner myScanner = new Scanner(System.in);
                option = myScanner.nextInt();
                myScanner.nextLine();


                switch (option){
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listarTop10PorIdioma();
                        break;
                    case 3:
                        listarLibrosRegistrado();
                        break;
                    case 4:
                        listarLibrosPorIdioma();
                        break;
                    case 5:
                        listarLibrosPorAutor();
                        break;
                    case 6:
                        listarAutoresRegistrados();
                        break;
                    case 7:
                        listarAutoresVivosEnAno();
                        break;
                    case 0:
                        System.out.println("Gracias por usar el servicio");
                        break;
                    default:
                        System.out.println("Opcion no encontrada, intenten nuevamente");
                }
            }
        }catch (InputMismatchException e){
            System.out.println("Opcion no encontrada, intente denuevo");
        }
    }

    private void buscarLibroPorTitulo(){
        System.out.println("Escriba el nombre del susodicho libro: ");
        var nombreLibro = teclado.nextLine();

        try{
            var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
            Datos datos = conversor.obtenerDatos(json, Datos.class);
            DatosLibro datosLibro = datos.resultados().get(0);
            registrarLibroEnBaseDatos(datosLibro);

        } catch (DataIntegrityViolationException e){
            System.out.println("\n :: El libro solicitado ya se encuentra registrado.\n");
        }catch (IndexOutOfBoundsException e) {
            System.out.println("No hay info referente a su busqueda, intente nuevamente");
        }
    }

    private void listarLibrosRegistrado(){
        libros = repositorio.findAll();
        cantidadLibros = libros.size();
        System.out.println("\nLIBROS REGISTRADOS EN LITERALURA: \n");
        if(cantidadLibros>0){
            System.out.println("\n:: Cantidad de Libros Registrados: " + cantidadLibros + " libro/s \n");
            libros.stream().forEach(System.out::println);
        }else{
            System.out.println("No hay info referente a su busqueda, intente nuevamente");
        }

    }

    private void listarAutoresRegistrados(){
        autores = autorRepositorio.findAll();
        cantidadAutores = autores.size();
        if(cantidadAutores>0){
            System.out.println("\nAUTORES REGISTRADOS EN LITERALURA: \n");
            System.out.println("\n:: Cantidad de Autores Registrados: " + cantidadAutores + " autor/es \n");
            autores.stream().forEach(System.out::println);
        }else{
            System.out.println("No hay info referente a su busqueda, intente nuevamente");
        }
    }

    private void listarAutoresVivosEnAno(){
        System.out.println("\n-> Escriba el año que desea buscar: \n");
        Integer anoBuscado = teclado.nextInt();

        autores = autorRepositorio.autoresVivosEnAno(anoBuscado);
        cantidadAutores = autores.size();

        if(cantidadAutores>0){
            System.out.printf("\n:: Cantidad de Autores Vivos en %d:  %s autor/es \n", anoBuscado, cantidadAutores);
            autores.stream().forEach(System.out::println);
        }else{
            System.out.println("No hay info referente a su busqueda, intente nuevamente");
        }
    }

    private void listarLibrosPorIdioma(){
        System.out.println("""
                Escoja el idioma que desea buscar:
                
                es : spanish
                en : english
                it : italian
                fr : french
                
                """);
        String idiomaElegido = teclado.nextLine();

        if(!idiomaElegido.equals("en") && !idiomaElegido.equals("fr") && !idiomaElegido.equals("it") && !idiomaElegido.equals("es")){
            System.out.println("Opcion no encontrada, intente denuevo");
        }else {
            System.out.println("\nLIBROS REGISTRADOS EN EL IDIOMA ELEGIDO : \n");

            libros = repositorio.listarLibrosPorIdioma(idiomaElegido);
            cantidadLibros = libros.size();

            if(cantidadLibros > 0){
                System.out.println("\n:: Cantidad de libros registrados: " + cantidadLibros + " libro/s" + "\n");
                libros.stream().forEach(System.out::println);
            }else{
                System.out.println("No hay info referente a su busqueda, intente nuevamente");
            }
        }
    }

    private void listarLibrosPorAutor(){
        System.out.println("\n-> Escriba el autor que desea buscar: \n");
        String autorABuscar = teclado.nextLine();

        libros = repositorio.listarLibrosPorAutor(autorABuscar);
        cantidadLibros = libros.size();

        if(cantidadLibros>0){
            System.out.printf("\n:: Hay registrados %d libro/s de %s \n", cantidadLibros , autorABuscar.toUpperCase());
            libros.stream().forEach(System.out::println);
        }else{
            System.out.println("No hay info referente a su busqueda, intente nuevamente");
        }

    };

    private void listarTop10PorIdioma() {
        System.out.println("""
                Escoja el idioma que desea buscar:
                
                es : spanish
                en : english
                it : italian
                fr : french
                
                """);
        String idiomaElegido = teclado.nextLine();

        if ( !idiomaElegido.equals("fr") && !idiomaElegido.equals("it") && !idiomaElegido.equals("en") && !idiomaElegido.equals("es")) {
            System.out.println("Opcion no encontrada, intente denuevo");
        } else {
            try{
                var jsonLanguage = consumoAPI.obtenerDatos(URL_BASE+"?languages="+ idiomaElegido);
                Datos datosPorIdioma = conversor.obtenerDatos(jsonLanguage, Datos.class);

                List<DatosLibro> datosLibros =datosPorIdioma.resultados().stream()
                        .limit(10)
                        .collect(Collectors.toList());

                libros = datosLibros.stream()
                        .map(l-> new Libro(l, idiomaElegido))
                        .collect(Collectors.toList());

                System.out.println(datosLibros);
                System.out.println("\nLOS 10 MEJORES LIBROS EN TU IDIOMA ELEGIDO");
                System.out.println("Fuente experta: Gutendex\n");
                libros.forEach(System.out::println);

                datosLibros.forEach(l -> registrarLibroEnBaseDatos(l));

            }catch (IndexOutOfBoundsException e){
                e.getMessage();
            }

        }
    }

    private void registrarLibroEnBaseDatos(DatosLibro datosLibro){
        if(datosLibro.autor().isEmpty()){
            try{
                Libro libroARegistrar = new Libro(datosLibro);
                repositorio.save(libroARegistrar);
                System.out.println("\n :: " +libroARegistrar.getTitulo() + " fue registrado con éxito.\n");
            } catch(DataIntegrityViolationException e){
                System.out.println("\n :: El libro " + " ya se encuentra registrado.\n");
            }

        }else{

            try{
                DatosAutor datosAutor = datosLibro.autor().get(0);
                Libro libroARegistrar = new Libro(datosLibro);
                Autor autorARegistrar = new Autor(datosAutor);
                List<Autor> autorBuscado = autorRepositorio.buscarAutorPorNombre(autorARegistrar.getNombre());
                if(autorBuscado.size()==0){
                    autorRepositorio.save(autorARegistrar);
                    System.out.println("\n :: " +libroARegistrar.getTitulo() + " y su autor fueron registrados con éxito.\n");
                }else{
                    repositorio.save(libroARegistrar);
                    System.out.println("\n :: " +libroARegistrar.getTitulo() + " fue registrado con éxito.\n");
                }

            } catch (DataIntegrityViolationException e){
                System.out.println("\n :: El libro " + " ya se encuentra registrado.\n");
            }
        }


    }

}
