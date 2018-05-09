/*
 Errores con el cambio de genes en la nueva poblacion... ruletaRandom()
 */
package algoritmos_geneticos_2;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author marco
 */
public class Algoritmos_Geneticos_2 {
        

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int[] variables = new int[4];
        int count = 0;
        int igualdad=0;
        int poblacion =0;
        int longitud_cromosomas = 4;
        Scanner scanner = new Scanner(System.in);
        
        //Insercion de datos por el usuario
        System.out.println(" Introduzca valores de a,b,c,d para la ecuacion: a + bx + cy + dz");
        
        while(count != longitud_cromosomas){
            variables[count] = scanner.nextInt();
            count++;
        }
        
        System.out.println(" Introduzca el valor de la igualdad");
        igualdad = scanner.nextInt();
        
        System.out.println("Introduzca la cantidad de individuos para la poblacion inicial");
        poblacion = scanner.nextInt();
        
        int genes = poblacion * longitud_cromosomas;
        int[] array_total_genes = new int[genes];
        
        for (int i=0; i<array_total_genes.length ;i++){     // Se crea un array con todos los genes de la poblacion inicial
            array_total_genes[i] = generar_Poblacion_Inicial(igualdad);  
        }
        mostrar_Poblacion_Inicial(array_total_genes,longitud_cromosomas);  // Mostrar la poblacion inicial
        
        int[] evaluacion = probar_Funcion_Objetivo(array_total_genes,poblacion,longitud_cromosomas); //Generar el arreglo con los valos de la funcion objetivo para realizar Fitness
        
        double[] probabilidad = probabilidad_Acumulativa(evaluacion,poblacion); //Fitness y Probabilidad Acumulativa
        
        int[] nueva_poblacion = ruleta_Random(poblacion,probabilidad ,array_total_genes);
        mostrar_Poblacion_Inicial(nueva_poblacion,longitud_cromosomas);
        
        int[] nuevo_array_total_genes = crossover(poblacion,longitud_cromosomas ,nueva_poblacion);
        
        nuevo_array_total_genes = mutacion(genes,nuevo_array_total_genes,igualdad,longitud_cromosomas);

    }
    
    
    
    
    
    
    
    
    
    
    public static int generar_Poblacion_Inicial(int igualdad){ // Entre 0 y el valor de la igualdad
            int randomNum = ThreadLocalRandom.current().nextInt(0, igualdad + 1);
            return randomNum;
        }
    
    public static double generar_Numero_Random(){  // Entre 0 y 1 para la ruleta random
        double random = ThreadLocalRandom.current().nextDouble(0, 1);
        return random;
    }
    
    public static int generar_Punto_Cruce(int longitud_cromosomas){ // Ejemplo: desde el 1 al 3
        int randomNum = ThreadLocalRandom.current().nextInt(1, longitud_cromosomas - 1);
            return randomNum;
    }
    
    public static int generar_Gen_Reemplazar_Mutacion(int genes){
        int randomNum = ThreadLocalRandom.current().nextInt(1, genes+ 1);
            return randomNum;
    }
    
    public static void mostrar_Poblacion_Inicial(int[] array_total_genes, int longitud_cromosomas){
        int i =0 ; 
        int j =1;
        while(i< array_total_genes.length-longitud_cromosomas){
            System.out.print(" C["+ j + "] = ");
            System.out.print(array_total_genes[i] + "," + array_total_genes[i+1] + "," + array_total_genes[i+2] + "," + array_total_genes[i+3] + ",");
            System.out.println("");
            i += longitud_cromosomas;
            j++;
        }
    }
    
    public static int[] probar_Funcion_Objetivo(int[] array_total_genes, int poblacion,int longitud_cromosomas){   // Se gurdan los valores de la funcion objetivo de cada individuo en un nuevo array
        int[] evaluacion = new int[poblacion];
        int i =0;
        int j =0;
        int y= 0;
        System.out.println("Evaluacion en la funcion objetivo:");
        while(y < poblacion){
            j=0;
          while(j<longitud_cromosomas){
        evaluacion[y] += array_total_genes[i];
        i++;
        j++;
          }
          System.out.println(evaluacion[y]);
          y++;
        }
        return evaluacion;
    }
    
    public static double[] probabilidad_Acumulativa(int[] evaluacion,int poblacion){  // Me va a devolver de una el array de Probabilidad Acumulativa por temas de practicidad.
        double[] fitness = new double[poblacion];
        double [] probabilidad = new double[poblacion];
        int i = 0;
        double fitness_total = 0;
        //Fitness y Fitness Total
        while(i<poblacion){
            double numero = 0;
            numero = (double) 1 / evaluacion[i];
            fitness[i] = numero;
            fitness_total += numero;
            
            System.out.println(" Fitness["+ i + "] = " + fitness[i]);
            i++;
        }
        System.out.println("Fitness Total = " + fitness_total);
        i=0;
        // Probabilidad Acumulativa
        while(i < poblacion){
            probabilidad[i] = fitness[i]/ fitness_total;
            System.out.println(" Probabilidad[" + i + "]= " + probabilidad[i]);
            i++;
        }
        
        return probabilidad;
    }
    
    public static int[] ruleta_Random(int poblacion, double[] probabilidad , int[] array_total_genes){
        double[] ruleta = new double[poblacion];
        int[] nueva_poblacion = array_total_genes; 
        int i =0 ;
        while( i < poblacion){ //Llenar el array random desde el 0 al 1
            ruleta[i] = generar_Numero_Random();
            System.out.println("Ruleta["+ i+"]=" + ruleta[i]);
            i++;
        }
        i = 0 ;
        int j=0;
        while(i < poblacion){
           if (ruleta[i] > probabilidad[poblacion -1 -i-j]){
               System.out.println("Ruleta >" + probabilidad[poblacion -1 - i -j]);
               nueva_poblacion[i]= array_total_genes[probabilidad.length-i+1];
               nueva_poblacion[i+1]=array_total_genes[probabilidad.length-i+2];
               nueva_poblacion[i+2]=array_total_genes[probabilidad.length-i+3];
               nueva_poblacion[i+3]=array_total_genes[probabilidad.length-i+4];
               i++;
               j=0;}
           else if(ruleta[i] < probabilidad[0]){
               System.out.println("Ruleta else if >" + probabilidad[poblacion -1 - i -j]);
                   nueva_poblacion[i] = array_total_genes[0];
                   nueva_poblacion[i+1]=array_total_genes[1];
                   nueva_poblacion[i+2]=array_total_genes[2];
                   nueva_poblacion[i+3]=array_total_genes[3];
                   i++;
                   j = 0;
               }
           else
           j++;
        }
        return nueva_poblacion;
    }
    
    public static int[] crossover(int poblacion, int longitud_cromosomas , int[] nueva_poblacion){
        int i = 0 ;
        double tasa_de_cruce = 0.25;
        int contador_seleccionados = 0;
        int[] nuevo_array_total_genes = nueva_poblacion;
        int[] seleccionados = new int[poblacion];
        double[] ruleta = new double[poblacion];
        
        while( i < poblacion){ //Llenar el array random desde el 0 al 1 NUEVAMENTE
            ruleta[i] = generar_Numero_Random();
            if(ruleta[i] < tasa_de_cruce){       //Llenar un array con los numeros de los seleccionados.
                seleccionados[contador_seleccionados]= i;
                contador_seleccionados ++;
            }
            i++;
        }
          
        i = 0; // Me servira para que no me de error al moverme en el array de seleccionados
        int j = 0; // Me servira para obtener el valor del array de seleccionados
        int k = 1; // Me servira para obtener los valores de la poblacion
        while(i < contador_seleccionados){ //Cambio los genes de uno seleccionado por el otro seleccionado
            int rand = generar_Punto_Cruce(longitud_cromosomas);
            k = longitud_cromosomas - rand;
            if(i+1 < contador_seleccionados){
            while(k>0)
            nuevo_array_total_genes[seleccionados[i]*longitud_cromosomas - longitud_cromosomas + k] =  nueva_poblacion[seleccionados[i+1]*longitud_cromosomas - longitud_cromosomas + k];
               k--;
            }
            else if(i+1>=contador_seleccionados){
                j=0;
                while(k>0)
            nuevo_array_total_genes[seleccionados[i]*longitud_cromosomas - longitud_cromosomas + k] =  nueva_poblacion[seleccionados[j]*longitud_cromosomas - longitud_cromosomas + k];
               k--;
               j++;
            }
        }
        
        return nuevo_array_total_genes;
    }
    
    public static int[] mutacion(int genes,int[] nuevo_array_total_genes, int igualdad,int longitud_cromosomas){
        int mutar = generar_Gen_Reemplazar_Mutacion(genes);
        nuevo_array_total_genes[mutar] = generar_Poblacion_Inicial(igualdad);
        mostrar_Poblacion_Inicial(nuevo_array_total_genes, longitud_cromosomas);  //Mostrar nueva funcion objetivo
        
        return nuevo_array_total_genes;
    }
    
}
