package base.api.service;

import base.api.dto.request.TrainerCreationRequest;
import base.api.model.Staff;

public interface StaffService {
    Staff createTrainer(TrainerCreationRequest request);
}