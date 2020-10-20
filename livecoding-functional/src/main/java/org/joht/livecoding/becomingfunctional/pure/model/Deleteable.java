package org.joht.livecoding.becomingfunctional.pure.model;

/**
 * Kennzeichnet loeschbare Eintraege.
 * 
 * @author JohT
 */
public interface Deleteable {

	/**
	 * Trifft zu, wenn der Eintrag geloescht ist.
	 * 
	 * @return <code>true</code>, wenn zutreffend.
	 */
	boolean isGeloescht();

	/**
	 * Loescht den Eintrag.
	 */
	void loeschen();
}
