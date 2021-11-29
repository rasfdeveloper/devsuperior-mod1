package com.renatofranco.customers.services;

import com.renatofranco.customers.dto.ClientDTO;
import com.renatofranco.customers.entities.Client;
import com.renatofranco.customers.repositories.ClientRepository;
import com.renatofranco.customers.services.exceptions.DatabaseException;
import com.renatofranco.customers.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    private static final String RESOURCE_NOT_FOUND = "Resource not found";
    private static final String INTEGRITY_VIOLATION = "Data integrity violation";

    @Transactional(readOnly = true)
    public Page<ClientDTO> findAllPageable(PageRequest pageRequest){
        var customers = clientRepository.findAll(pageRequest);
        return customers.map(ClientDTO::new);
    }

    @Transactional(readOnly = true)
    public ClientDTO findById(long id) {
        var optionalCustomer = clientRepository.findById(id);
        var customer = optionalCustomer.orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND));

        return new ClientDTO(customer);
    }

    @Transactional
    public ClientDTO save(ClientDTO clientDTO){
        var customer = new Client();
        mapDtoToCustomer(clientDTO, customer);
        return new ClientDTO(clientRepository.save(customer));
    }

    @Transactional
    public ClientDTO update(long id, ClientDTO clientDTO){

        try {
            var customer = clientRepository.getOne(id);
            mapDtoToCustomer(clientDTO, customer);
            return new ClientDTO(clientRepository.save(customer));
        }
        catch (EntityNotFoundException e){
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
        }

    }

    @Transactional
    public void delete(long id){
        try {
            clientRepository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND);
        }
        catch (DataIntegrityViolationException e){
            throw new DatabaseException(INTEGRITY_VIOLATION);
        }
    }

    public void mapDtoToCustomer(ClientDTO clientDTO, Client client){
        client.setName(clientDTO.getName());
        client.setCpf(clientDTO.getCpf());
        client.setBirthDate(clientDTO.getBirthDate());
        client.setChildren(clientDTO.getChildren());
        client.setIncome(clientDTO.getIncome());

    }

}
