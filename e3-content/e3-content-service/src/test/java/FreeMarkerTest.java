
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import pojo.Student;

public class FreeMarkerTest {

	@Test
	public void testFreeMarker() throws Exception {
		//1、创建一个模板文件
		//2、创建一个Configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		//3、设置模板文件保存的目录
		configuration.setDirectoryForTemplateLoading(new File("C:\\This is D\\testworkspace\\e3-parent\\e3-content\\e3-content-service\\src\\test\\resources\\ftl"));
		//4、模板文件的编码格式，一般就是utf-8
		configuration.setDefaultEncoding("utf-8");
		//5、加载一个模板文件，创建一个模板对象。
		Template template = configuration.getTemplate("hello.ftl");
		//6、创建一个数据集。可以是pojo也可以是map。推荐使用map
		Map<String, String> data = new HashMap<>();
		data.put("hello", "hello freemarker!");
		//7、创建一个Writer对象，指定输出文件的路径及文件名。
		Writer out = new FileWriter(new File("C:\\This is D\\testworkspace\\e3-parent\\e3-content\\e3-content-service\\src\\test\\resources\\ftl\\hello.txt"));
		//8、生成静态页面
		template.process(data, out);
		//9、关闭流
		out.close();
	}
	
	@Test
	public void testFreeMarkerGrammar() throws Exception {
		Configuration configuration = new Configuration(Configuration.getVersion());
		configuration.setDirectoryForTemplateLoading(new File("C:\\This is D\\testworkspace\\e3-parent\\e3-content\\e3-content-service\\src\\test\\resources\\ftl"));
		configuration.setDefaultEncoding("utf-8");
		Template template = configuration.getTemplate("Student.ftl");
		
		HashMap<String, Object> dataModel = new HashMap<String, Object>();
		Student student = new Student();
		student.setId(1);
		student.setAge(24);
		student.setHobby("编码");
		student.setName("shopping");
		student.setBirthday(new Date());
		dataModel.put("stu", student);
		
		ArrayList<Student> stuList = new ArrayList<Student>();
		for (int i = 0; i < 10; i++) {
			Student stu = new Student();
			stu.setId(i+1);
			stu.setAge(24);
			stu.setHobby(i%2==0?"":"编码");
			stu.setName("shopping");
			stu.setBirthday(new Date());
			stuList.add(stu);
		}
		dataModel.put("stuList", stuList);
		dataModel.put("hello", "hello freemarker!");
		FileWriter fileWriter = new FileWriter(new File("C:\\This is D\\testworkspace\\e3-parent\\e3-content\\e3-content-service\\src\\test\\resources\\ftl\\student.html"));
		template.process(dataModel, fileWriter);
		fileWriter.close();
	}
}
