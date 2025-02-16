
package com.mrhouse.mrhouse.servicios;

import com.mrhouse.mrhouse.entidades.Usuario;
import com.mrhouse.mrhouse.enumeraciones.Rol;
import com.mrhouse.mrhouse.repositorios.RepositorioUsuario;
import com.mrhouse.mrhouse.excepciones.MiException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author thell
 */  
@Service
public class ServicioUsuario  implements UserDetailsService{
  

    @Autowired
    private RepositorioUsuario repositorioUsuario;
 
    @Transactional
    public void registrar(MultipartFile archivo, String nombre, String email, 
            String password, String password2) throws MiException{
        validar(nombre, email, password, password2);
        Usuario usuario =new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.setRol(Rol.USER);
   
        repositorioUsuario.save(usuario);
    }
      public void actualizar(MultipartFile archivo, String idUsuario, String nombre, String email, String password, String password2) throws Exception{
          validar(nombre, email, password, password2);
          Optional<Usuario> respuesta = repositorioUsuario.findById(idUsuario);
          if(respuesta.isPresent()){
              Usuario usuario = respuesta.get();
              usuario.setNombre(nombre);
              usuario.setEmail(email);
            usuario.setPassword(password);
                  usuario.setRol(Rol.USER);
                  String idImagen=null;
       repositorioUsuario.save(usuario);
   
          }
          
          
      }
      
             public Usuario getOne(String id){
          
          return repositorioUsuario.getOne(id);
      }
              @Transactional()
    public List<Usuario> listarUsuarios() {

        List<Usuario> usuarios = new ArrayList();

        usuarios = repositorioUsuario.findAll();

        return usuarios;
    }
       @Transactional
    public void cambiarRol(String id){
        Optional<Usuario> respuesta = repositorioUsuario.findById(id);
    	
    	if(respuesta.isPresent()) {
    		
    		Usuario usuario = respuesta.get();
    		
    		if(usuario.getRol().equals(Rol.USER)) {
    			
    		usuario.setRol(Rol.ADMIN);
    		
    		}else if(usuario.getRol().equals(Rol.ADMIN)) {
    			usuario.setRol(Rol.USER);
    		}
    	}
    }
       public void validar( String nombre, String email, String password, String password2) throws MiException{
           if(nombre==null || nombre.isEmpty()){
               throw new MiException("el nombre no puede estar vacio o ser nulo");
           }
           if(email==null || email.isEmpty()){
                throw new MiException("el email no puede estar vacio o ser nulo");
           }
           if(password==null || password.isEmpty() || password.length()<=5){
                throw new MiException("el password no puede estar vacio o ser nulo y debe tener mas de 5 caracteres");
           }
           if(password2==null || password2.isEmpty()){
                  throw new MiException("el password no puede estar vacio o ser nulo");
           }
           if(!password.equals(password2)){
                  throw new MiException("Las contraseñas deben ser iguales");
           }
       }
  
}

