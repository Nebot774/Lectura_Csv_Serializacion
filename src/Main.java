import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    private final static String delimitador=",";

    public static void main(String[] args) {

        try(Stream<String> contenidoFunkos= Files.lines(Path.of("/home/adrcle/Descargas/funkos.csv"))){

            List<Funko> listaFunkos=contenidoFunkos
                    .map(l->{
                        List<String> funkows=Arrays.asList(l.split(delimitador));

                        String COD=funkows.get(0);
                        String nombre=funkows.get(1);
                        String modelo=funkows.get(2);
                        String precio=funkows.get(3);
                        String fecha_lanzamiento=funkows.get(4);

                        return new Funko(COD,nombre,modelo,precio,fecha_lanzamiento);

                    }).toList();



            Optional<Funko> funkoMasCaro = listaFunkos.stream()
                    .filter(funko -> {
                        try {
                            Double.parseDouble(funko.getPrecio());
                            return true; // El precio es un número válido
                        } catch (NumberFormatException e) {
                            return false; // El precio no es un número válido
                        }
                    })
                    .max(Comparator.comparingDouble(funko -> Double.parseDouble(funko.getPrecio())));





            //encontrar la media de precio de los funkos
            //utilizamos optional para ahorrar la implementacion de un is present
            OptionalDouble mediaFunkos = listaFunkos.stream()
                    .mapToDouble(funko -> Double.valueOf(funko.getPrecio())) // Convierte las cadenas a valores double
                    .average(); // Calcula la media

            System.out.println("La media del precio de los Funkos es:"+mediaFunkos);


            // Imprimir los Funkos agrupados por modelo
            //hazemos un mapeo de el array y una coleccion para agrupar los funkos por modelos
            Map<String, List<Funko>> funkosPorModelos=listaFunkos.stream()
                    .collect(Collectors.groupingBy(Funko::getModelo));

            //hazemos un bucle for each de funkos por modelo
            funkosPorModelos.forEach((modelo, listaFunkosPorModelo) -> {//el lamba coge dos expressiones
                System.out.println("Modelo: " + modelo);//mostramos el modelo
                listaFunkos.forEach(funko -> System.out.println("   " + funko));//hazemos otro bucle en el que recorremos
            });                                                                 //el array de objetos funko principal  e imprimimos los que sean de ese modelo



            //NUMEROS DE FUNKO POR MODELO
            Map<String, Long> cantidadFunkosPorModelo = listaFunkos.stream()
                    .collect(Collectors.groupingBy(Funko::getModelo, Collectors.counting()));

            // Imprimir la cantidad de Funkos por modelo
            cantidadFunkosPorModelo.forEach((modelo, cantidad) -> {
                System.out.println("Modelo: " + modelo + ", Cantidad: " + cantidad);
            });

            //funkos lanzados en 2023
            List<Funko> funkosLanzadosEn2023 = listaFunkos.stream()
                    .filter(funko -> funko.getFechaLanzamiento().endsWith("2023"))
                    .collect(Collectors.toList());

            System.out.println("Funkos lanzados en 2023:");
            funkosLanzadosEn2023.forEach(funko -> System.out.println("   " + funko.getNombre()));



        }catch(IOException e){
            e.printStackTrace(System.out);
        }






    }

}






