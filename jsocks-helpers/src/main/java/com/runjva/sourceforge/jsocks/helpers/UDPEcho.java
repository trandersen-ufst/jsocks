package com.runjva.sourceforge.jsocks.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Plain SOCKS unaware UDP echo server and client.
 */
public class UDPEcho implements Runnable {

    private final int port;
    private final InetAddress hostIP;
    private final DatagramSocket sock;

    private static final int BUF_SIZE = 1024;

    public UDPEcho(String host, int port) throws IOException,
            UnknownHostException {
        this.hostIP = InetAddress.getByName(host);
        this.port = port;
        sock = new DatagramSocket();
        System.out.println("UDP: " + sock.getLocalAddress() + ":"
                + sock.getLocalPort());
        // sock.connect(hostIP,port);
    }

    public void send(String s) throws IOException {
        System.out.println("Sending:" + s);
        final DatagramPacket packet = new DatagramPacket(s.getBytes(), s
                .length(), hostIP, port);
        sock.send(packet);
    }

    public void run() {
        final byte[] buf = new byte[BUF_SIZE];
        final DatagramPacket incomingData = new DatagramPacket(buf, buf.length);
        try {
            while (true) {
                sock.receive(incomingData);
                System.out.println("UDP From:"
                        + incomingData.getAddress().getHostAddress() + ":"
                        + incomingData.getPort());
                System.out.println(new String(incomingData.getData(), 0,
                        incomingData.getLength()));
                System.out.flush();
                incomingData.setLength(buf.length);
            }
        } catch (final IOException io_ex) {
            io_ex.printStackTrace();
        }
    }

    public static void usage() {
        System.err.print("Usage: java UDPEcho host port\n" + "OR\n"
                + "Usage: java UDPEcho port\n");
    }

    public static void doEcho(int port) throws IOException {
        final byte[] buf = new byte[BUF_SIZE];
        final DatagramPacket packet = new DatagramPacket(buf, buf.length);
        final DatagramSocket sock = new DatagramSocket(port);

        System.out.println("Starting UDP echo on"
                + sock.getLocalAddress().getHostAddress() + ":"
                + sock.getLocalPort());
        while (true) {
            try {
                sock.receive(packet);
                sock.send(packet);
                System.out.print("UDP From: "
                        + packet.getAddress().getHostAddress() + ":"
                        + packet.getPort() + "\n"
                        + new String(packet.getData(), 0, packet.getLength())
                        + "\n");
                System.out.flush();

                packet.setLength(buf.length);
                // packet = new DatagramPacket(buf,buf.length);
            } catch (final IOException io_ex) {
            }
        }
    }

    public static void main(String args[]) {
        int port;
        String host;

        if (args.length == 1) {
            try {
                port = Integer.parseInt(args[0]);

                doEcho(port);

            } catch (final IOException io_ex) {
                io_ex.printStackTrace();
                System.exit(1);

            } catch (final NumberFormatException num_ex) {
                num_ex.printStackTrace();
                System.exit(1);
            }
        } else if (args.length == 2) {
            try {
                host = args[0];
                port = Integer.parseInt(args[1]);

                final UDPEcho ut = new UDPEcho(host, port);
                final Thread thread = new Thread(ut);
                thread.start();

                final BufferedReader in = new BufferedReader(
                        new InputStreamReader(System.in));
                String s;
                System.out.print("Enter datagram:");
                s = in.readLine();
                while (s != null) {
                    ut.send(s);
                    try {
                        Thread.currentThread();
                        Thread.sleep(100);
                    } catch (final InterruptedException i_ex) {
                    }
                    System.out.print("Enter datagram:");
                    s = in.readLine();
                }
                System.exit(1);

            } catch (final IOException io_ex) {
                io_ex.printStackTrace();
                System.exit(1);
            } catch (final NumberFormatException num_ex) {
                num_ex.printStackTrace();
                System.exit(1);
            }

        } else {
            usage();
        }
    }

}// End of class
