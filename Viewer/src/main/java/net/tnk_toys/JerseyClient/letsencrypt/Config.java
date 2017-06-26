package net.tnk_toys.JerseyClient.letsencrypt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Config {
	
	public static final String SUB_COMMAND_RUN              = "run";
	public static final String SUB_COMMAND_CERTONLY         = "certonly";
	public static final String SUB_COMMAND_INSTALL          = "install";
	public static final String SUB_COMMAND_RENEW            = "renew";
	public static final String SUB_COMMAND_REVOKE           = "revoke";
	public static final String SUB_COMMAND_REGISTER         = "register";
	public static final String SUB_COMMAND_ROLLBACK         = "rollback";
	public static final String SUB_COMMAND_CONFIG_CHANGES   = "config_changes";
	public static final String SUB_COMMAND_PLUGINS          = "plugins";
	

	public static final String OPTION_HELP           = "-h";
	public static final String OPTION_SERVER         = "-s";
	public static final String OPTION_CONFIG         = "-c";
	public static final String OPTION_VERBOSE1       = "-v";
	public static final String OPTION_VERBOSE2       = "-vv";
	public static final String OPTION_VERBOSE3       = "-vvv";
	public static final String OPTION_TEXT           = "-t";
	public static final String OPTION_NONINTERACTIVE = "-n";
	public static final String OPTION_DIALOG         = "--dialog";
	public static final String OPTION_DRYRUN         = "--dry-run";
	public static final String OPTION_UPDATEREGISTRATION = "--update-registration";
	public static final String OPTION_EMAIL          = "-m";
	public static final String OPTION_DOMAIN         = "-d";
	public static final String OPTION_USERAGENT      = "--user-agent";
	

	public static final String AUTO_OPTION_KEEP      = "--keep";
	public static final String AUTO_OPTION_EXPAND      = "--expand";
	public static final String AUTO_OPTION_VERSION      = "--version";
	public static final String AUTO_OPTION_AGREETOS      = "--agree-tos";
	public static final String AUTO_OPTION_ACCOUNT      = "--account";
	public static final String AUTO_OPTION_DUPLICATE      = "--duplicate";
	public static final String AUTO_OPTION_QUIET      = "-q";
	
	public static final String SECURITY_OPTION_RSAKEYSIZE  = "--rsa-key-size";
	public static final String SECURITY_OPTION_REDIRECT  = "--redirect";
	public static final String SECURITY_OPTION_NOREDIRECT  = "--no-redirect";
	public static final String SECURITY_OPTION_HSTS  = "--hsts";
	public static final String SECURITY_OPTION_NOHSTS  = "--no-hsts";
	public static final String SECURITY_OPTION_UIR  = "--uir";
	public static final String SECURITY_OPTION_NOUIR  = "--no-uir";
	
	public static final String PROP_CERT_PATH = "--cert-path";
	
	
	
	public static final String DEFAULT_DOMAIN = "example.com"; 
	public static final String DEFAULT_SERVER = "https://acme-v01.api.letsencrypt.org/directory"; 
	public static final String DEFAULT_SUB_COMMAND   = SUB_COMMAND_CERTONLY;
	private static final String DEFAULT_COMMENT = "create path ";

	private static final String KEY_SUB_COMMAND = "LETSENCRYPT_SUB_COMMAND";
	private static final String KEY_SERVER = "LETSENCRYPT_SERVER";
	private static final String KEY_DOMAINS = "LETSENCRYPT_DOMAINS";


	private Path path = null;
	private Properties prop = null;
	
	public Config() throws IOException {
		this(Paths.get(System.getProperty("user.home")+File.separator+Config.class.getSimpleName()+".properties"));
	}

	public Config(Path path) throws IOException {
		this.path = path;
		if (Files.exists(this.path)) {
			try (BufferedReader br = Files.newBufferedReader(this.path)) {
				this.prop = new Properties();
				this.prop.load(br);
			}
		} else {
			try (BufferedWriter bw = Files.newBufferedWriter(this.path)) {
				this.prop = new Properties();
				this.prop.setProperty(KEY_SERVER, DEFAULT_SERVER);
				this.prop.setProperty(KEY_SUB_COMMAND, DEFAULT_SUB_COMMAND);
				this.prop.setProperty(KEY_DOMAINS, DEFAULT_DOMAIN);
				this.prop.store(bw, DEFAULT_COMMENT + this.path.toString());
			}
		}
	}

	public static void main(String[] args) {
		try {
			Config config = new Config();
			System.out.println(config.getServer());
			System.out.println(config.getSubCommand());
			System.out.println("domains = " + config.getDomains().length);
			System.out.println(String.join(",", config.getDomains()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setServer(String server) {
		prop.setProperty(Config.KEY_SERVER, server);
	}
	
	public String getServer() {
		return prop.getProperty(Config.KEY_SERVER);
	}
	
	public void setDomains(String[] domains) {
		
		prop.setProperty(Config.KEY_DOMAINS, String.join(",", domains));
	}
	
	public String[] getDomains() {
		return prop.getProperty(Config.KEY_DOMAINS).split(",");
	}
	
	public void setSubCommand(String subCommand) {
		prop.setProperty(Config.KEY_SUB_COMMAND, subCommand);
	}
	
	public String getSubCommand() {
		return prop.getProperty(Config.KEY_SUB_COMMAND);
	}
	
	public void reload() throws IOException {
		prop.stringPropertyNames().forEach(key->prop.remove(key));
		prop.clear();
		try (BufferedReader br = Files.newBufferedReader(this.path)) {
			prop.load(br);
		}
	}
}
