package App.BusinessLayer.Controllers;

import App.BusinessLayer.Services.VehicleService;
import App.DataLayer.Models.VehicleModel;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author cesar
 */

@RestController
// RequestMapping atiende las peticiones en la ruta dada por parametro
@RequestMapping("/api/vehicle")
// CrossOrigin permite el acceso desde paginas web diferentes a localhost
// Por ser entorno de pruebas se le da acceso a cualquier pagina web externa
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})

public class VehicleController {
    
    Logger logger = LoggerFactory.getLogger(UserController.class);
    
    // Autowired asigna un objeto a la instancia en el momento en el que
    // sea requerido

    @Autowired
    private VehicleService vehicleService;

    // GetMapping obtiene valores en una sub ruta dada como parametro
    @GetMapping
    public List<VehicleModel> findAll() {
        return vehicleService.findAll();
    }

    // GetMapping obtiene valores en una sub ruta dada como parametro
    @GetMapping("/{id}")
    public ResponseEntity<VehicleModel> findById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(vehicleService.findById(id));
        } catch (JsonParseException e) {
            logger.error(HttpStatus.BAD_REQUEST.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (EntityNotFoundException e) {
            logger.error(HttpStatus.NOT_FOUND.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        }

    }

    // PostMapping hace una peticion post a la ruta del controlador
    @PostMapping
    public ResponseEntity<VehicleModel> create(@RequestBody VehicleModel vehicleModel) {
        try {
            logger.trace(HttpStatus.CREATED.toString());
            return ResponseEntity.status(HttpStatus.CREATED).body(vehicleService.save(vehicleModel));
        } catch (Exception e) {
            logger.error(HttpStatus.BAD_REQUEST.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // PutMapping hace una peticion put a la ruta del controlador
    @PutMapping("/{id}")
    public ResponseEntity<VehicleModel> update(@RequestBody VehicleModel vehicleModel) {
        try {
            // Busqueda de prueba para saber si el registro ya existe
            VehicleModel vehicleModell = vehicleService.findById(vehicleModel.getIdVehicle());
            logger.trace(HttpStatus.CREATED.toString());
            return ResponseEntity.status(HttpStatus.CREATED).body(vehicleService.save(vehicleModel));

        } catch (JsonParseException e) {
            logger.error(HttpStatus.BAD_REQUEST.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (EntityNotFoundException e) {
            logger.error(HttpStatus.NOT_FOUND.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        }
    }

    // DeleteMapping hace una peticion delete a la ruta del controlador
    @DeleteMapping("/{id}")
    public ResponseEntity<VehicleModel> deleteById(@PathVariable int id) {

        try {
            vehicleService.deleteById(id);
            logger.trace(HttpStatus.OK.toString());
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (EmptyResultDataAccessException e) {
            logger.error(HttpStatus.NOT_FOUND.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (JsonParseException e) {
            logger.error(HttpStatus.BAD_REQUEST.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }
    
}