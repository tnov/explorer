package net.tnk_toys.JerseyClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ProcessSample {

	private static final String DEFAULT_ENCODE = "UTF-8";
	private String encode = null;
	private File basedir = null;
	
	public ProcessSample() {
		this.encode = DEFAULT_ENCODE;
		this.basedir = Paths.get(System.getProperty("user.dir")).toFile();
	}

	public ProcessSample(File basedir) {
		this.basedir = basedir;
	}
	
	public static void main(String[] args) {
		ProcessSample sample = new ProcessSample();
		List<String> command = new ArrayList<>();
		command.add("cmd");
		command.add("/c");
		command.add("dir");
		sample.encode = "Shift_JIS";
		System.out.println("result=" + sample.execute(command));
	}
	
	public int execute(List<String> command) {
		int result = 0;
		ProcessBuilder pb = new ProcessBuilder();
		pb.directory(basedir);
		pb.command(command);
//		pb.command("java","-version");
//		pb.command("cmd", "/c", "dir");
		Process process = null;
		try {
			process = pb.start();
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
//			BufferedInputStream bis = new BufferedInputStream(process.getErrorStream());
//		    int c;
//		    while ((c = bis.read()) != -1) {
//		      System.out.print((char) c);
//		    }
//			BufferedInputStream bis = new BufferedInputStream(process.getInputStream());
			InputStreamReader isr = new InputStreamReader(process.getInputStream(),encode);
			BufferedReader br = new BufferedReader(isr);
			while (br.ready()) {
				System.out.println(br.readLine());
			}
			result = process.exitValue();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				process.getInputStream().close();
				process.getOutputStream().close();
				process.getErrorStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			process.destroy();
		}
		return result;
	}

}
