package org.yearup.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Profile;
import org.yearup.repository.ProfileRepository;

import java.util.Optional;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;

    public Optional<Profile> getProfileByUserId(int userId) {
        return profileRepository.findById(userId);
    }

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Profile create(Profile profile) {
        if (profile == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return profileRepository.save(profile);
    }

    /**
     * Updates the user's profile information.
     * Throws NOT_Found
     *
     */
    public Profile updateProfile(int userId, Profile profile) {
        Profile existingProfile = getProfileByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        existingProfile.setFirstName(profile.getFirstName());
        existingProfile.setLastName(profile.getLastName());
        existingProfile.setPhone(profile.getPhone());
        existingProfile.setEmail(profile.getEmail());
        existingProfile.setAddress(profile.getAddress());
        existingProfile.setCity(profile.getCity());
        existingProfile.setState(profile.getState());
        existingProfile.setZip(profile.getZip());
        return profileRepository.save(existingProfile);
    }
}
