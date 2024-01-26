package com.example.treinoSpringLogin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;


@Controller
public class PaginasAcessiveis {
     private JdbcTemplate templateJdbc;
     private int verificado = 0;
    
    @Autowired
    public PaginasAcessiveis(JdbcTemplate templateJdbc) {
        this.templateJdbc = templateJdbc;
    }
    //checar se o usuário existe no banco de dados
    private int pesquisarUsuarioNoBd(String usuarioInserido, String senhaInserida) {
        //executar o comando SELECT usando como parâmetro o nome e senha inseridos e retornar se o usuário foi encontrado.
        String sql = "SELECT * FROM contas WHERE nome =\"" + usuarioInserido + "\" AND senha = \"" + senhaInserida + "\";";
        List<Map<String, Object>> resultadoPesquisa = templateJdbc.queryForList(sql);
        return resultadoPesquisa.size();
        //0 = usuário não existe; 1 = usuário encontrado
    }
    //redirecionar o usuário para a página de login caso o login não tenha sido feito corretamente (ou caso o usuário tente acessar a página através da url.)
    @GetMapping("/paginaConectado")
    public ModelAndView getConectado(){
        if(this.verificado != 1){
            return new ModelAndView("redirect:/paginaLogin");
        }
        ModelAndView modelAndView = new ModelAndView("conectado.html");
        return modelAndView;
    }
    //carregar arquivo html quando o usuário acessar a página de login
    @GetMapping("/paginaLogin")
    public ModelAndView getLogin() {
        this.verificado = 0;
        ModelAndView modelAndView = new ModelAndView("index.html");
        return modelAndView;
	}
    @PostMapping("/paginaLogin")
    public String postLogin(@RequestParam String campoUsuario, String campoSenha){
        this.verificado = pesquisarUsuarioNoBd(campoUsuario, campoSenha);
        if(this.verificado == 0){System.out.println("Usuário não encontrado.");}
        else{System.out.println("Usuario encontrado!");};
        return "redirect:paginaConectado";
    }

}