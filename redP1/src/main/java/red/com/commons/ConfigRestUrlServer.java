package red.com.commons;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix="rest")
@PropertySource(value="classpath:resturl.yml",factory = CompositeYmlSourceFactory.class)
public class ConfigRestUrlServer {
	
	private Protocol protocol = new Protocol();
	private Server server = new Server();

	@Override
	public String toString() {
		return "ConfigRestUrlServer [protocol=" + protocol + ", server=" + server + "]";
	}
}
