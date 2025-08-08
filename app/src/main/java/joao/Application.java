package joao;

import joao.adapter.in.web.UsersControllerAdapterIn;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;



@SpringBootApplication
// We use direct @Import instead of @ComponentScan to speed up cold starts
//@ComponentScan(basePackages = {"joao.core.usecase", "joao.adapter.out.persistence", "joao.config"})
@Import({ UsersControllerAdapterIn.class })
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}