/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udp;

/**
 *
 * @author LENOVO 720
 */
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class CEcoUDPS {

    public static void main(String[] args) {
        try {
            //datagra,socket sirve pa cliente y servidor
            DatagramSocket cl = new DatagramSocket();
            String mensaje = "";
            int pto = 2000;
            String dst = "127.0.0.1";

            while (!mensaje.equals("fin")) {

                System.out.print("Cliente iniciado, escriba el mensaje: ");

                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                mensaje = br.readLine();
                byte[] b = mensaje.getBytes();

                if (b.length > 20) {
                    ArrayList<byte[]> list = new ArrayList<byte[]>();
                    int numero_paquetes = b.length / 20, bandera_paquete = 0, k = 0;
                    float numero_paquetes_aux = (float) b.length / 20;

                    if (numero_paquetes_aux > (float) numero_paquetes) {
                        numero_paquetes++;
                        bandera_paquete = b.length % 20;
                    }
                    if (bandera_paquete != 0) {
                        for (int i = 0; i < numero_paquetes; i++) {
                            if (i == numero_paquetes - 1) {
                                list.add((byte[]) Arrays.copyOfRange(b, k, k + bandera_paquete));
                            } else {
                                byte[] arregloBaux = new byte[20];
                                System.arraycopy(b, 0 + k, arregloBaux, 0, 20);
                                list.add(arregloBaux);
                            }
                            k += 20;
                        }
                    } else {
                        for (int i = 0; i < numero_paquetes; i++) {
                            byte[] arregloBaux = new byte[20];
                            System.arraycopy(b, 0 + k, arregloBaux, 0, 20);
                            list.add(arregloBaux);
                            k += 20;
                        }
                    }
                    String tamP = String.valueOf(b.length);
                    DatagramPacket tam = new DatagramPacket(tamP.getBytes(), tamP.length(), InetAddress.getByName(dst), pto);
                    cl.send(tam);

                    for (int i = 0; i < numero_paquetes; i++) {

                        DatagramPacket p = new DatagramPacket(list.get(i), list.get(i).length, InetAddress.getByName(dst), pto);
                        cl.send(p);

                    }
                    //cl.close();
                } else {
                    String tamP = String.valueOf(b.length);
                    DatagramPacket tam = new DatagramPacket(tamP.getBytes(), tamP.length(), InetAddress.getByName(dst), pto);
                    cl.send(tam);
                    
                    DatagramPacket p = new DatagramPacket(b, b.length, InetAddress.getByName(dst), pto);
                    cl.send(p);
                    //cl.close();
                }
                String mensajeN = "";
                while (!mensajeN.equals("fin")) {
                    byte[] buffer = new byte[20];
                    DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);
                    cl.receive(peticion);
                    mensajeN = new String(peticion.getData());
                    System.out.print("Servidor:" + mensaje);
                    if (!mensajeN.equals("fin")) {
                        mensajeN = "";
                        break;
                    } else {

                        cl.close();
                    }
                }
                System.out.println("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }//catch
    }//main
}
