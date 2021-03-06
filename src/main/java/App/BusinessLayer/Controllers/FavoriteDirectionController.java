package App.BusinessLayer.Controllers;


import App.BusinessLayer.Pojo.FavoriteDirectionPOJO;
import App.BusinessLayer.Pojo.UserPOJO;
import App.BusinessLayer.Services.FavoriteDirectionService;
import App.BusinessLayer.Services.UserService;
import App.DataLayer.Models.FavoriteDirectionModel;
import App.DataLayer.Models.UserModel;
import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityNotFoundException;

@RestController
// RequestMapping atiende las peticiones en la ruta dada por parametro
@RequestMapping("/api/favorite-direction")
// CrossOrigin permite el acceso desde paginas web diferentes a localhost
// Por ser entorno de pruebas se le da acceso a cualquier pagina web externa
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST,
        RequestMethod.DELETE, RequestMethod.PUT})


public class FavoriteDirectionController {

    private FavoriteDirectionService favoriteDirectionService;
    private UserService userService;
    Logger logger = LoggerFactory.getLogger(FavoriteDirectionController.class);

    public FavoriteDirectionController(FavoriteDirectionService favoriteDirectionService, UserService userService) {
        this.favoriteDirectionService = favoriteDirectionService;
        this.userService = userService;
    }
/*
    public FavoriteDirectionModel fillModel(FavoriteDirectionPOJO favoriteDirectionPOJO,int idUser) {
        FavoriteDirectionModel favoriteDirection = new FavoriteDirectionModel();
        favoriteDirection.setIdUserFav(idUser);
        favoriteDirection.setFavAddress(favoriteDirectionPOJO.getFavAddress());
        favoriteDirection.setFavLatitude(favoriteDirectionPOJO.getFavLatitude());
        favoriteDirection.setFavLongitude(favoriteDirectionPOJO.getFavLongitude());
        favoriteDirection.setDatetimeCreationFav(favoriteDirectionPOJO.getDateTimeCreationFav());
        favoriteDirection.setNameFd(favoriteDirectionPOJO.getNameFd());
        return favoriteDirection;
    }
    public FavoriteDirectionPOJO fillPOJO(FavoriteDirectionModel favoriteDirectionModel) {
        FavoriteDirectionPOJO favoriteDirection = new FavoriteDirectionPOJO();
        favoriteDirection.setIdFavoriteDirection(favoriteDirectionModel.getIdFavDest());
        favoriteDirection.setDateTimeCreationFav(favoriteDirectionModel.getDatetimeCreationFav());
        favoriteDirection.setFavAddress(favoriteDirectionModel.getFavAddress());
        favoriteDirection.setFavLatitude(favoriteDirectionModel.getFavLatitude());
        favoriteDirection.setFavLongitude(favoriteDirectionModel.getFavLongitude());
        favoriteDirection.setNameFd(favoriteDirectionModel.getNameFd());
        return favoriteDirection;
    }*/

    @PostMapping(value = {"/new-direction"})
    public ResponseEntity<Void> addDirectionToUser(@RequestBody FavoriteDirectionPOJO pojo) {
        String username =
                SecurityContextHolder.getContext().getAuthentication().getName();
        UserModel existingUser = userService.findByUserMail(username);
        try {
            logger.error(pojo.getModel(existingUser.getIdUser()).toString());
            //fillModel(pojo,existingUser.getIdUser())
            // .toString());
            existingUser.addDirection(favoriteDirectionService.save(pojo.getModel(existingUser.getIdUser())));
            logger.trace(HttpStatus.CREATED.toString());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error(HttpStatus.BAD_REQUEST.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = {"/delete-direction"})
    public ResponseEntity<Void> deleteDirectionvyUser(@RequestBody FavoriteDirectionPOJO pojo) {
        String username =
                SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            favoriteDirectionService.deleteById(pojo.getIdFavoriteDirection());
            return new ResponseEntity<>(HttpStatus.CONTINUE);
        } catch (EntityNotFoundException e) {
            logger.error(HttpStatus.NOT_FOUND.toString());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    @GetMapping("/show-directions")
    public List<FavoriteDirectionPOJO> getDirectiosByUser() {
        String email =
                SecurityContextHolder.getContext( ).getAuthentication( ).getName( );
        UserModel user = userService.findByUserMail( email );
        List<FavoriteDirectionModel> favoriteDirectionModels = favoriteDirectionService.getDirectionsByUser(user);
        List<FavoriteDirectionPOJO> favoriteDirections = new ArrayList<>();
        for (FavoriteDirectionModel favoriteDirection : favoriteDirectionModels) {
            favoriteDirections.add(new FavoriteDirectionPOJO(favoriteDirection));
        }
        return favoriteDirections;
    }

}
