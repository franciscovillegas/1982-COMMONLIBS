package portal.com.eje.tools.sqlfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import org.springframework.core.io.ClassPathResource;

import portal.com.eje.portal.factory.Weak;

public class SqlResourceTool {

	public static SqlResourceTool getInstance() {
		return Weak.getInstance(SqlResourceTool.class);
	}

	public String getSql(String path) {
		StringBuilder stringBuilder = new StringBuilder();

		try {
			ClassPathResource resource = new ClassPathResource(path);
			BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()), 1024);

			String line;

			while ((line = br.readLine()) != null) {
				stringBuilder.append(line).append("\n");
			}
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}

}
