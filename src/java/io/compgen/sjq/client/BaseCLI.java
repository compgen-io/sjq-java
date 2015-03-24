package io.compgen.sjq.client;

import io.compgen.annotation.Exec;
import io.compgen.annotation.Option;
import io.compgen.exceptions.CommandArgumentException;
import io.compgen.sjq.support.StringUtils;

import java.io.IOException;

public abstract class BaseCLI {
	private String connFile = null;
	private int port = -1;
	private String host = null;
	private String passwd = "";
	
	@Option(name="port", desc="Port to listen on (default: dynamic)", charName="p")
	public void setPort(int port) {
		this.port = port;
	}
	
	@Option(name="host", desc="Hostname/IP address to listen on (default: dynamic)", charName="h")
	public void setHost(String host) {
		this.host = host;
	}


	@Option(name="conn", desc="File with IP:port", charName="f")
	public void setConnFile(String fname) {
		this.connFile = fname;
	}

	@Option(name="passwd", desc="Set a password for the job-queue")
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	@Option(name="passwdfile", desc="Set a password for the job-queue (read from file)")
	public void setPasswdFile(String filename) throws IOException {
		this.passwd = StringUtils.strip(StringUtils.readFile(filename));
	}


	@Exec
	public void exec() throws Exception {
		if (host == null && port == -1 && connFile == null) {
			throw new CommandArgumentException("You must specify host and port or a connection file.");
		}
		
		if (connFile != null) {
			String conn = StringUtils.strip(StringUtils.readFile(connFile));
			String[] hostport = conn.split(":",2);
			host = hostport[0];
			port = Integer.parseInt(hostport[1]);
		}
//		
//		System.err.println("Connecting to: " + host + ":" + port);

		SJQClient client = new SJQClient(host, port, passwd);
		process(client);
	}

	protected abstract void process(SJQClient client);
}
