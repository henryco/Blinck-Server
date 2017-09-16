package net.henryco.blinckserver.unit.helper;

import net.henryco.blinckserver.mvc.model.entity.relation.core.embeded.Type;
import net.henryco.blinckserver.unit.BlinckUnitTest;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Henry on 16/09/17.
 */
public class TypeFormTest extends BlinckUnitTest {


	@Test
	public void typeCheckerFemMaleTest() {

		Type type1 = new Type();
		type1.setDimension(3);
		type1.setIdent(Type.FEMALE);
		type1.setWanted(Type.MALE);

		Type checked1 = Type.typeChecker(type1);

		Assert.assertEquals(new Integer(3), checked1.getDimension());
		Assert.assertEquals(Type.MALE, checked1.getWanted());
		Assert.assertEquals(Type.FEMALE, checked1.getIdent());
	}


	@Test
	public void typeCheckerMaleFemTest() {

		Type type2 = new Type();
		type2.setDimension(5);
		type2.setIdent(Type.MALE);
		type2.setWanted(Type.FEMALE);

		Type checked2 = Type.typeChecker(type2);

		Assert.assertEquals(new Integer(5), checked2.getDimension());
		Assert.assertEquals(Type.FEMALE, checked2.getWanted());
		Assert.assertEquals(Type.MALE, checked2.getIdent());
	}


	@Test
	public void typeCheckerBothTest() {

		Type type1 = new Type();
		type1.setDimension(3);
		type1.setIdent(Type.BOTH);
		type1.setWanted(Type.BOTH);

		Type checked1 = Type.typeChecker(type1);

		Assert.assertEquals(new Integer(3), checked1.getDimension());
		Assert.assertEquals(Type.BOTH, checked1.getWanted());
		Assert.assertEquals(Type.BOTH, checked1.getIdent());
	}

	@Test
	public void typeCheckerWrongBothTest1() {

		Type type1 = new Type();
		type1.setDimension(3);
		type1.setIdent(Type.FEMALE);
		type1.setWanted(Type.BOTH);

		Type checked1 = Type.typeChecker(type1);

		Assert.assertEquals(new Integer(3), checked1.getDimension());
		Assert.assertEquals(Type.BOTH, checked1.getWanted());
		Assert.assertEquals(Type.BOTH, checked1.getIdent());
	}


	@Test
	public void typeCheckerWrongBothTest2() {

		Type type1 = new Type();
		type1.setDimension(3);
		type1.setIdent(Type.BOTH);
		type1.setWanted(Type.MALE);

		Type checked1 = Type.typeChecker(type1);

		Assert.assertEquals(new Integer(3), checked1.getDimension());
		Assert.assertEquals(Type.BOTH, checked1.getWanted());
		Assert.assertEquals(Type.BOTH, checked1.getIdent());
	}

	@Test
	public void typeCheckerMaleMaleTest() {

		Type type1 = new Type();
		type1.setDimension(5);
		type1.setIdent(Type.MALE);
		type1.setWanted(Type.MALE);

		Type checked1 = Type.typeChecker(type1);

		Assert.assertEquals(new Integer(5), checked1.getDimension());
		Assert.assertEquals(Type.MALE, checked1.getWanted());
		Assert.assertEquals(Type.MALE, checked1.getIdent());
	}

	@Test
	public void typeCheckerFemFemTest() {

		Type type1 = new Type();
		type1.setDimension(5);
		type1.setIdent(Type.FEMALE);
		type1.setWanted(Type.FEMALE);

		Type checked1 = Type.typeChecker(type1);

		Assert.assertEquals(new Integer(5), checked1.getDimension());
		Assert.assertEquals(Type.FEMALE, checked1.getWanted());
		Assert.assertEquals(Type.FEMALE, checked1.getIdent());
	}

}