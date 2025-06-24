package br.edu.atitus.api_sample.services;

import br.edu.atitus.api_sample.entities.EventEntity;
import br.edu.atitus.api_sample.entities.PointEntity;
import br.edu.atitus.api_sample.repositories.EventRepository;
import br.edu.atitus.api_sample.repositories.PointRepository;
import br.edu.atitus.api_sample.dtos.EventDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private PointRepository pointRepository;

    public List<EventEntity> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<EventEntity> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public EventEntity createEvent(EventDTO eventDTO) throws Exception {
        EventEntity event = new EventEntity();
        BeanUtils.copyProperties(eventDTO, event);
        
        if (eventDTO.getLocationId() != null) {
            PointEntity location = pointRepository.findById(eventDTO.getLocationId())
                .orElseThrow(() -> new Exception("Location not found"));
            event.setLocation(location);
        }
        
        return eventRepository.save(event);
    }

    public EventEntity updateEvent(Long id, EventDTO eventDTO) throws Exception {
        EventEntity event = eventRepository.findById(id)
            .orElseThrow(() -> new Exception("Event not found"));

        event.setName(eventDTO.getName());
        event.setDescription(eventDTO.getDescription());
        event.setCreator(eventDTO.getCreator());
        
        if (eventDTO.getLocationId() != null) {
            PointEntity location = pointRepository.findById(eventDTO.getLocationId())
                .orElseThrow(() -> new Exception("Location not found"));
            event.setLocation(location);
        }

        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}
