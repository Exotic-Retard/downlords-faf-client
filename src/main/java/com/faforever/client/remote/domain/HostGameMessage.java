package com.faforever.client.remote.domain;

import com.faforever.client.game.GameVisibility;

/**
 * Data sent from the client to the FAF server to tell it about a preferences to be hosted.
 */
public class HostGameMessage extends ClientMessage {

  private String mapname;
  private String title;
  private String mod;
  private boolean[] options;
  private GameAccess access;
  private Integer version;
  private String password;
  private GameVisibility visibility;

  public HostGameMessage(GameAccess gameAccess, String mapName, String title, boolean[] options, String mod, String password, Integer version) {
    super(ClientMessageType.HOST_GAME);
    access = gameAccess;
    this.mapname = mapName;
    this.title = title;
    this.options = options;
    this.mod = mod;
    this.password = password;
    this.version = version;
    this.visibility = GameVisibility.PUBLIC;
  }

  public String getMapname() {
    return mapname;
  }

  public void setMapname(String mapname) {
    this.mapname = mapname;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getMod() {
    return mod;
  }

  public void setMod(String mod) {
    this.mod = mod;
  }

  public boolean[] getOptions() {
    return options;
  }

  public void setOptions(boolean[] options) {
    this.options = options;
  }

  public GameAccess getAccess() {
    return access;
  }

  public void setAccess(GameAccess access) {
    this.access = access;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public GameVisibility getVisibility() {
    return visibility;
  }

  public void setVisibility(GameVisibility visibility) {
    this.visibility = visibility;
  }
}
