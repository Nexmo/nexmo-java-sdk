/*
 *   Copyright 2023 Vonage
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.vonage.client.meetings;

import com.vonage.client.HttpWrapper;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MeetingsClient {
	final GetAvailableRoomsEndpoint getAvailableRooms;
	final GetRoomEndpoint getRoom;
	final CreateRoomEndpoint createRoom;
	final UpdateRoomEndpoint updateRoom;
	final GetThemeRoomsEndpoint getThemeRooms;
	final GetThemesEndpoint getThemes;
	final GetThemeEndpoint getTheme;
	final CreateThemeEndpoint createTheme;
	final UpdateThemeEndpoint updateTheme;
	final DeleteThemeEndpoint deleteTheme;
	final GetRecordingsEndpoint getRecordings;
	final GetRecordingEndpoint getRecording;
	final DeleteRecordingEndpoint deleteRecording;
	final GetDialNumbersEndpoint getDialNumbers;
	final GetLogoUploadUrlsEndpoint getLogoUploadUrls;
	final FinalizeLogosEndpoint finalizeLogos;
	final UpdateApplicationEndpoint updateApplication;

	/**
	 * Constructor.
	 *
	 * @param httpWrapper (REQUIRED) shared HTTP wrapper object used for making REST calls.
	 */
	public MeetingsClient(HttpWrapper httpWrapper) {
		getAvailableRooms = new GetAvailableRoomsEndpoint(httpWrapper);
		getRoom = new GetRoomEndpoint(httpWrapper);
		createRoom = new CreateRoomEndpoint(httpWrapper);
		updateRoom = new UpdateRoomEndpoint(httpWrapper);
		getThemeRooms = new GetThemeRoomsEndpoint(httpWrapper);
		getThemes = new GetThemesEndpoint(httpWrapper);
		getTheme = new GetThemeEndpoint(httpWrapper);
		createTheme = new CreateThemeEndpoint(httpWrapper);
		updateTheme = new UpdateThemeEndpoint(httpWrapper);
		deleteTheme = new DeleteThemeEndpoint(httpWrapper);
		getRecordings = new GetRecordingsEndpoint(httpWrapper);
		getRecording = new GetRecordingEndpoint(httpWrapper);
		deleteRecording = new DeleteRecordingEndpoint(httpWrapper);
		getDialNumbers = new GetDialNumbersEndpoint(httpWrapper);
		getLogoUploadUrls = new GetLogoUploadUrlsEndpoint(httpWrapper);
		finalizeLogos = new FinalizeLogosEndpoint(httpWrapper);
		updateApplication = new UpdateApplicationEndpoint(httpWrapper);
	}

	static UUID validateThemeId(UUID themeId) {
		return Objects.requireNonNull(themeId,  "Theme ID is required.");
	}

	static UUID validateRoomId(UUID roomId) {
		return Objects.requireNonNull(roomId,  "Room ID is required.");
	}

	static String validateSessionId(String sessionId) {
		if (sessionId == null || sessionId.trim().isEmpty()) {
			throw new IllegalArgumentException("Session ID cannot be null or empty.");
		}
		return sessionId;
	}

	static String validateRecordingId(String recordingId) {
		if (recordingId == null || recordingId.trim().isEmpty()) {
			throw new IllegalArgumentException("Recording ID cannot be null or empty.");
		}
		return recordingId;
	}


	/**
	 * Get all available rooms in the application.
	 *
	 * @param startId The ID to start returning events at (inclusive).
	 * @param endId The ID to end returning events at (exclusive).
	 * @param pageSize Number of results per page.
	 *
	 * @return The HAL response.
	 */
	public GetRoomsResponse getAvailableRooms(Integer startId, Integer endId, Integer pageSize) {
		return getAvailableRooms.execute(new GetRoomsRequest(startId, endId, pageSize, null));
	}

	/**
	 * Get details of an existing room.
	 *
	 * @param roomId ID of the room to retrieve.
	 *
	 * @return The meeting room associated with the ID.
	 */
	public MeetingRoom getRoom(UUID roomId) {
		return getRoom.execute(validateRoomId(roomId));
	}

	/**
	 * Create a new room.
	 *
	 * @param room Properties of the meeting room.
	 *
	 * @return Details of the created meeting room.
	 */
	public MeetingRoom createRoom(MeetingRoom room) {
		return createRoom.execute(Objects.requireNonNull(room, "Meeting room is required."));
	}

	/**
	 * Update an existing room.
	 *
	 * @param roomId ID of the meeting room to be updated.
	 * @param roomUpdate Properties of the meeting room to change.
	 *
	 * @return Details of the updated meeting room.
	 */
	public MeetingRoom updateRoom(UUID roomId, UpdateRoomRequest roomUpdate) {
		Objects.requireNonNull(roomUpdate, "Room update request properties is required.");
		roomUpdate.roomId = validateRoomId(roomId);
		return updateRoom.execute(roomUpdate);
	}

	/**
	 * Get rooms that are associated with a theme ID.
	 *
	 * @param themeId The theme ID to filter by.
	 * @param startId The ID to start returning events at (inclusive).
	 * @param endId The ID to end returning events at (exclusive).
	 *
	 * @return The HAL response.
	 */
	public GetRoomsResponse getThemeRooms(UUID themeId, Integer startId, Integer endId) {
		return getThemeRooms.execute(new GetRoomsRequest(
				startId, endId, null, validateThemeId(themeId))
		);
	}

	/**
	 * Get all application themes.
	 *
	 * @return The list of themes.
	 */
	public List<Theme> getThemes() {
		return getThemes.execute(null);
	}

	/**
	 * Retrieve details of a theme by ID.
	 *
	 * @param themeId The theme ID.
	 *
	 * @return The theme associated with the ID.
	 */
	public Theme getTheme(UUID themeId) {
		return getTheme.execute(validateThemeId(themeId));
	}

	/**
	 * Create a new theme.
	 *
	 * @param theme The partial theme properties.
	 *
	 * @return The full created theme details.
	 */
	public Theme createTheme(Theme theme) {
		Objects.requireNonNull(theme, "Theme creation properties are required.");
		Objects.requireNonNull(theme.getBrandText(), "Brand text is required.");
		Objects.requireNonNull(theme.getMainColor(), "Main color is required.");
		return createTheme.execute(theme);
	}

	/**
	 * Update an existing theme.
	 *
	 * @param themeId ID of the theme to update.
	 * @param theme The partial theme properties to update.
	 *
	 * @return The full updated theme details.
	 */
	public Theme updateTheme(UUID themeId, Theme theme) {
		Objects.requireNonNull(theme, "Theme update properties are required.");
		theme.themeId = validateThemeId(themeId);
		return updateTheme.execute(theme);
	}

	/**
	 * Delete a theme by its ID.
	 *
	 * @param themeId ID of the theme to delete.
	 * @param force Whether to delete the theme even if theme is used by rooms or as application default theme.
	 */
	public void deleteTheme(UUID themeId, boolean force) {
		deleteTheme.execute(new DeleteThemeRequest(validateThemeId(themeId), force));
	}

	/**
	 * Get recordings of a meeting session.
	 *
	 * @param sessionId The session ID to filter recordings by.
	 *
	 * @return The list of recordings for the session.
	 */
	public List<Recording> getRecordings(String sessionId) {
		return getRecordings.execute(validateSessionId(sessionId)).getRecordings();
	}

	/**
	 * Get details of a recording.
	 *
	 * @param recordingId ID of the recording to retrieve.
	 *
	 * @return The recording properties.
	 */
	public Recording getRecording(String recordingId) {
		return getRecording.execute(validateRecordingId(recordingId));
	}

	/**
	 * Delete a recording.
	 *
	 * @param recordingId ID of the recording to delete.
	 */
	public void deleteRecording(String recordingId) {
		deleteRecording.execute(validateRecordingId(recordingId));
	}

	/**
	 * Get numbers that can be used to dial into a meeting.
	 *
	 * @return The list of dial-in numbers, along with their country code.
	 */
	public List<DialNumbers> getDialNumbers() {
		return getDialNumbers.execute(null);
	}

	/**
	 * Get URLs that can be used to upload logos for a theme via a POST.
	 *
	 * @return List of URLs and respective credentials / tokens needed for uploading logos to them.
	 */
	public List<LogoUploadsUrlResponse> getLogoUploadUrls() {
		return getLogoUploadUrls.execute(null);
	}

	/**
	 * Change logos to be permanent for a given theme.
	 *
	 * @param themeId The theme ID containing the logos.
	 * @param keys List of temporary theme's logo keys to make permanent
	 */
	public void finalizeLogos(UUID themeId, List<String> keys) {
		if (keys == null || keys.isEmpty()) {
			throw new IllegalArgumentException("Logo keys are required.");
		}
		finalizeLogos.execute(new FinalizeLogosRequest(validateThemeId(themeId), keys));
	}

	/**
	 * Update an existing application.
	 *
	 * @param updateRequest Properties of the application to update.
	 *
	 * @return The updated application details.
	 */
	public Application updateApplication(UpdateApplicationRequest updateRequest) {
		return updateApplication.execute(Objects.requireNonNull(
				updateRequest, "Application update properties are required.")
		);
	}
}
