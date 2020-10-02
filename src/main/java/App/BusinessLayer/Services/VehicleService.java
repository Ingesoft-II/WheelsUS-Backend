package App.BusinessLayer.Services;

import App.BusinessLayer.Repositories.VehicleRepository;
import App.DataLayer.Models.VehicleModel;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author cesar
 */
@Service
public class VehicleService {
    
    @Autowired
    private VehicleRepository vehicleRepository;

    public List<VehicleModel> findAll(){
        return (List<VehicleModel>) vehicleRepository.findAll();
    }

    public VehicleModel findById(int id){
        return vehicleRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public VehicleModel save(VehicleModel vehicleModel){
        return vehicleRepository.save(vehicleModel);
    }

    public void deleteById(int id){
        vehicleRepository.deleteById(id);
    }
    
}