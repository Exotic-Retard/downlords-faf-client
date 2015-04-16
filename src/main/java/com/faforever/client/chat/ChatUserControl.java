package com.faforever.client.chat;

import com.faforever.client.fxml.FxmlLoader;
import com.faforever.client.games.CreateGameDialogController;
import com.faforever.client.i18n.I18n;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;

import static com.faforever.client.fx.WindowDecorator.WindowButtonType.CLOSE;

public class ChatUserControl extends HBox {

  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static final String CLAN_TAG_FORMAT = "[%s]";

  @Autowired
  FxmlLoader fxmlLoader;

  @Autowired
  AvatarService avatarService;

  @Autowired
  CountryFlagService countryFlagService;

  @Autowired
  I18n i18n;

  @Autowired
  UserInfoWindowFactory userInfoWindowFactory;

  @FXML
  ImageView countryImageView;

  @FXML
  private ImageView avatarImageView;

  @FXML
  ImageView statusImageView;

  @FXML
  Label usernameLabel;

  @FXML
  Label clanLabel;

  private final PlayerInfoBean playerInfoBean;
  private Tooltip avatarTooltip;
  private Tooltip countryTooltip;
  private Stage userInfoWindow;

  public ChatUserControl(PlayerInfoBean playerInfoBean) {
    this.playerInfoBean = playerInfoBean;
  }

  @FXML
  void initialize() {
    configureCountryImageView();
    configureAvatarImageView();
    configureClanLabel();
    configureContextMenu();
  }

  @FXML
  void onContextMenuRequested() {
    ContextMenu contextMenu = new ContextMenu();

    MenuItem userInfoItem = new MenuItem(i18n.get("chat.userContext.info"));
    userInfoItem.setOnAction(event -> onUserInfo());

    contextMenu.getItems().add(userInfoItem);
    contextMenu.getItems().add(new MenuItem(i18n.get("chat.userContext.privateMessage")));
    contextMenu.getItems().add(new MenuItem(i18n.get("chat.userContext.addFriend")));
    contextMenu.getItems().add(new MenuItem(i18n.get("chat.userContext.addFoe")));
    contextMenu.getItems().add(new MenuItem(i18n.get("chat.userContext.invite")));
    contextMenu.getItems().add(new MenuItem(i18n.get("chat.userContext.kick")));
    contextMenu.getItems().add(new MenuItem(i18n.get("chat.userContext.ban")).);
  }

  private void onUserInfo() {
    if (userInfoWindow != null) {
      return;
    }

    UserInfoWindow controller = userInfoWindowFactory.create(userInfo);

    userInfoWindow = new Stage(StageStyle.TRANSPARENT);
    userInfoWindow.initModality(Modality.NONE);
    userInfoWindow.initOwner(this.stage);

    sceneFactory.createScene(userInfoWindow, controller.getRoot(), false, CLOSE);

    userInfoWindow.setOnHiding(event -> {
      userInfoWindow = null;
    });

    userInfoWindow.show();
  }

  private void configureContextMenu() {

  }

  private void configureClanLabel() {
    playerInfoBean.clanProperty().addListener((observable, oldValue, newValue) -> {
      if (StringUtils.isEmpty(newValue)) {
        clanLabel.setVisible(false);
      } else {
        clanLabel.setText(String.format(CLAN_TAG_FORMAT, newValue));
        clanLabel.setVisible(true);
      }
    });
  }

  private void configureAvatarImageView() {
    setAvatarUrl(playerInfoBean.getAvatarUrl());

    avatarImageView.setOnMouseMoved(event -> {
      if (avatarTooltip != null) {
        return;
      }
      avatarTooltip = new Tooltip();
      avatarTooltip.textProperty().bind(playerInfoBean.avatarTooltipProperty());
      avatarTooltip.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_BOTTOM_RIGHT);
      avatarTooltip.show(((Node) event.getTarget()).getScene().getWindow(), event.getScreenX(), event.getScreenY());
      event.consume();
    });
    avatarImageView.setOnMouseExited(event -> {
      avatarTooltip.hide();
      avatarTooltip = null;
      event.consume();
    });
    playerInfoBean.avatarUrlProperty().addListener((observable, oldValue, newValue) -> {
      setAvatarUrl(newValue);
    });
  }

  private void configureCountryImageView() {
    setCountry(playerInfoBean.getCountry());

    countryImageView.setOnMouseEntered(event -> {
      if (countryTooltip != null) {
        return;
      }
      countryTooltip = new Tooltip();
      countryTooltip.textProperty().bind(playerInfoBean.countryProperty());
      countryTooltip.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_BOTTOM_RIGHT);
      countryTooltip.show(((Node) event.getTarget()).getScene().getWindow(), event.getScreenX(), event.getScreenY());
      event.consume();
    });
    countryImageView.setOnMouseExited(event -> {
      countryTooltip.hide();
      countryTooltip = null;
      event.consume();
    });
    playerInfoBean.countryProperty().addListener((observable, oldValue, newValue) -> {
      setCountry(newValue);
    });
  }

  private void setCountry(String country) {
    if (StringUtils.isEmpty(country)) {
      countryImageView.setVisible(false);
    } else {
      countryImageView.setImage(countryFlagService.loadCountryFlag(country));
      countryImageView.setVisible(true);
    }
  }

  private void setAvatarUrl(String avatarUrl) {
    if (StringUtils.isEmpty(avatarUrl)) {
      avatarImageView.setVisible(false);
    } else {
      avatarImageView.setImage(avatarService.loadAvatar(avatarUrl));
      avatarImageView.setVisible(true);
    }
  }

  @PostConstruct
  void init() {
    fxmlLoader.loadCustomControl("chat_user_control.fxml", this);
    usernameLabel.setText(playerInfoBean.getUsername());
  }

  public PlayerInfoBean getPlayerInfoBean() {
    return playerInfoBean;
  }
}
