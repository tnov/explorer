/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.tnk_toys.JerseyClient;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.Arrays;

/**
 *
 * @author dell
 */
public class DriveInfo {
    public static void main(String[] args) {
        System.out.println("test");
        Arrays.asList(File.listRoots()).stream().forEach(System.out::println);
        FileSystems.getDefault().getRootDirectories().forEach(System.out::println);
    }
}
