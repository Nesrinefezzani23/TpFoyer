package tn.esprit.tpfoyer.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.dto.BlocDTO;
import tn.esprit.tpfoyer.entities.Bloc;
import tn.esprit.tpfoyer.mappers.BlocMapper; // ⬅️ Import
import tn.esprit.tpfoyer.repositories.BlocRepository;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class BlocServiceImpl implements IBlocService{
    final BlocRepository blocRepository;
    final BlocMapper blocMapper;

    @Override
    public Bloc addOrUpdateBloc(Bloc bloc) {
        return blocRepository.save(bloc);
    }

    @Override
    public void deleteBloc(long id) {
        blocRepository.deleteById(id);
    }

    @Override
    public List<BlocDTO> findAllBloc() {
        return blocRepository.findAll().stream()
                .map(blocMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BlocDTO findBlocById(long id) {
        Bloc bloc = blocRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bloc non trouvé"));

        return blocMapper.toDto(bloc); // Conversion automatique
    }
}