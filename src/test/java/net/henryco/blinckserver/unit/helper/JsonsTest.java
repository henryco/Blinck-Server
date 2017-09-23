package net.henryco.blinckserver.unit.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.henryco.blinckserver.configuration.project.notification.BlinckNotification;
import net.henryco.blinckserver.mvc.model.entity.infrastructure.ReportList;
import net.henryco.blinckserver.mvc.model.entity.profile.embeded.priv.PrivateProfile;
import net.henryco.blinckserver.mvc.model.entity.profile.embeded.pub.bio.BioEntity;
import net.henryco.blinckserver.mvc.model.entity.profile.embeded.pub.bio.UserNameEntity;
import net.henryco.blinckserver.mvc.model.entity.profile.embeded.pub.media.MediaEntity;
import net.henryco.blinckserver.mvc.model.entity.profile.embeded.pub.media.UserPhotoEntity;
import net.henryco.blinckserver.mvc.model.entity.relation.core.Party;
import net.henryco.blinckserver.mvc.model.entity.relation.core.SubParty;
import net.henryco.blinckserver.mvc.model.entity.relation.core.embeded.Details;
import net.henryco.blinckserver.mvc.model.entity.relation.core.embeded.Meeting;
import net.henryco.blinckserver.mvc.model.entity.relation.core.embeded.Type;
import net.henryco.blinckserver.mvc.model.entity.security.UserAuthProfile;
import net.henryco.blinckserver.mvc.service.profile.UserBaseProfileService;
import net.henryco.blinckserver.mvc.service.profile.UserImageMediaService;
import net.henryco.blinckserver.mvc.service.relation.core.PartyService;
import net.henryco.blinckserver.mvc.service.relation.core.SubPartyService;
import net.henryco.blinckserver.mvc.service.relation.queue.PartyMeetingOfferService;
import net.henryco.blinckserver.unit.BlinckUnitTest;
import org.junit.Test;

import java.util.ArrayList;

/**
 * This is not regular test class
 * i used it for checking jsons forms
 * @author Henry on 22/09/17.
 */
public class JsonsTest extends BlinckUnitTest{


	private static String write(Object object) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		return object.getClass().getSimpleName() + ":\n\n"+ mapper.writeValueAsString(object) + "\n\n";
	}

	@Test
	public void bioEntity() throws Exception {

		BioEntity object = new BioEntity();
		object.setUserName(new UserNameEntity());

		System.out.println(write(object));
	}

	@Test
	public void mediaEntity() throws Exception {
		MediaEntity object = new MediaEntity();
		object.setPhoto(new UserPhotoEntity());

		System.out.println(write(object));
	}

	@Test
	public void nameDetails() throws Exception {
		UserBaseProfileService.NameDetails object = new UserBaseProfileService.NameDetails();
		object.setUser(new UserNameEntity());

		System.out.println(write(object));
	}

	@Test
	public void privateProfile() throws Exception {
		System.out.println(write(new PrivateProfile()));
	}

	@Test
	public void userImageInfoTest() throws Exception {
		System.out.println(write(new UserImageMediaService.UserImageInfo()));
	}

	@Test
	public void notificationTest() throws Exception {
		System.out.println(write(new BlinckNotification.JsonNotificationForm()));
	}

	@Test
	public void typeFormTest() throws Exception {
		System.out.println(write(new Type()));
	}

	@Test
	public void subPartyInfoTest() throws Exception {
		SubPartyService.SubPartyInfo info = new SubPartyService.SubPartyInfo();
		info.setType(new Type());
		info.setUsers(new ArrayList<>());
		System.out.println(write(info));
	}

	@Test
	public void partyInfoTest() throws Exception {
		PartyService.PartyInfo info = new PartyService.PartyInfo();
		info.setSubParties(new ArrayList<>());
		info.setUsers(new ArrayList<>());
		info.setMeeting(new Meeting());
		System.out.println(write(info));
	}

	@Test
	public void meetingTest() throws Exception {
		System.out.println(write(new Meeting()));
	}

	@Test
	public void offerInfoTest() throws Exception {
		PartyMeetingOfferService.OfferInfo info = new PartyMeetingOfferService.OfferInfo();
		info.setOffer(new Meeting());
		System.out.println(write(info));
	}

	@Test
	public void reportListTest() throws Exception {
		System.out.println(write(new ReportList()));
	}

	@Test
	public void simpNotifTest() throws Exception {
		System.out.println(write(new BlinckNotification.SimpleNotification()));
	}

	@Test
	public void authProfileTest() throws Exception {

		System.out.println(write(new UserAuthProfile()));
	}

	@Test
	public void partyTest() throws Exception {
		Party party = new Party();
		party.setMeeting(new Meeting());
		Details details = new Details();
		details.setType(new Type());
		party.setDetails(details);
		party.setSubParties(new ArrayList<>());

		System.out.println(write(party));
	}

	@Test
	public void subPartyTest() throws Exception {

		Party party = new Party();
		party.setMeeting(new Meeting());
		Details details = new Details();
		details.setType(new Type());
		party.setDetails(details);
		party.setSubParties(new ArrayList<>());

		SubParty subParty = new SubParty();
		subParty.setParty(party);
		subParty.setDetails(details);
		subParty.setUsers(new ArrayList<>());

		System.out.println(write(subParty));
	}


}