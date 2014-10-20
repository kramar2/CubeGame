package com.example.panklocek;

import static org.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
//import org.andengine.examples.PhysicsExample;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
//import org.andengine.examples.TMXTiledMapExample;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXObject;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import android.opengl.GLES20;
import android.content.Context;
import android.widget.Toast;

public class PanKlocekGame extends SimpleBaseGameActivity implements IAccelerationListener, IOnSceneTouchListener {

	// ===========================================================
	// Constants
	// ===========================================================
	// CAMERA
	private static final int CAMERA_WIDTH = 2534;
	private static final int CAMERA_HEIGHT = 1520;

	
	private BoundCamera mBoundChaseCamera;
	
	
	// ===========================================================
	// Fields
	// ===========================================================

    private BitmapTextureAtlas keyObiectsAtlas;
    private BitmapTextureAtlas obiectsAtlas;
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private BitmapTextureAtlas mBitmapTextureAtlas2;


    private ITextureRegion playerMainTexture;
    private ITextureRegion playerAlternativeTexture;
	
	private ITextureRegion mFaceTextureRegion;
    private ITextureRegion mFaceAlternativeTextureRegion;

	private BitmapTextureAtlas mOnScreenControlTexture;
	private ITextureRegion mOnScreenControlBaseTextureRegion;
	private ITextureRegion mOnScreenControlKnobTextureRegion;

	private boolean mPlaceOnScreenControlsAtDifferentVerticalLocations = false;
	
		
	protected TMXTiledMap mTMXTiledMap;
	
	protected int mCactusCount;
	protected int mWallCount;
	protected int loopCount;
	public String tekst1="";
	
	protected Scene mScene;
	protected PhysicsWorld mPhysicsWorld;
	private static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);

    private TiledTextureRegion key1;
    private TiledTextureRegion key2;
    private TiledTextureRegion key3;
    private TiledTextureRegion key4;
    private TiledTextureRegion key5;

	private TiledTextureRegion mBoxFaceTextureRegion;
	private TiledTextureRegion mCircleFaceTextureRegion;
	private TiledTextureRegion mTriangleFaceTextureRegion;
	private TiledTextureRegion mHexagonFaceTextureRegion;
	private int mFaceCount = 0;
	
	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	
	public Vector2 playerCoords=new Vector2(128.0f,128.0f);
	public List<Vector2> trianglesCoords= new ArrayList<Vector2>(); 
	public List<Vector2> circlesCoords= new ArrayList<Vector2>(); 
	public List<Vector2> hexagonsCoords= new ArrayList<Vector2>(); 
	public List<Vector2> crossesCoords = new ArrayList<Vector2>();
    public List<Vector2> starCoords = new ArrayList<Vector2>();


    public final Map<String,Rectangle> gateMap=new HashMap<String, Rectangle>();
	public final Map<String,PhysicsConnector> gateBodyMap=new HashMap<String, PhysicsConnector>();
	public final Map<String,Vector2> gateDataMap=new HashMap<String, Vector2>();
	
	public final Map<String,Rectangle> circleSwitchMap=new HashMap<String, Rectangle>();
	public final Map<String,Rectangle> triangleSwitchMap=new HashMap<String, Rectangle>();
	public final Map<String,Rectangle> hexagonSwitchMap=new HashMap<String, Rectangle>();
	public final Map<String,Rectangle> boxSwitchMap=new HashMap<String, Rectangle>();
	
	public final Map<String,String> circleSwitchConMap=new HashMap<String, String>();
	public final Map<String,String> triangleSwitchConMap=new HashMap<String, String>();
	public final Map<String,String> hexagonSwitchConMap=new HashMap<String, String>();
	public final Map<String,String> boxSwitchConMap=new HashMap<String, String>();
	
	
	
	public final List<AnimatedSprite> triangleSprites=new ArrayList<AnimatedSprite>();
	public final List<AnimatedSprite> circleSprites=new ArrayList<AnimatedSprite>();
	public final List<AnimatedSprite> hexagonSprites=new ArrayList<AnimatedSprite>();
	public final List<AnimatedSprite> crossSprites =new ArrayList<AnimatedSprite>();
    public final List<AnimatedSprite> starSprites=new ArrayList<AnimatedSprite>();
	
	
	public final List<Rectangle> exitFieldList= new ArrayList<Rectangle>();
	
	
	
	public static String missionName="tmx/mission04.tmx";   
	
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		this.mBoundChaseCamera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mBoundChaseCamera);
		engineOptions.getTouchOptions().setNeedsMultiTouch(true);

		if(MultiTouch.isSupported(this)) {
			if(MultiTouch.isSupportedDistinct(this)) {
				Toast.makeText(this, "MultiTouch detected --> Both controls will work properly!", Toast.LENGTH_SHORT).show();
			} else {
				this.mPlaceOnScreenControlsAtDifferentVerticalLocations = true;
				Toast.makeText(this, "MultiTouch detected, but your device has problems distinguishing between fingers.\n\nControls are placed at different vertical locations.", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(this, "Sorry your device does NOT support MultiTouch!\n\n(Falling back to SingleTouch.)\n\nControls are placed at different vertical locations.", Toast.LENGTH_LONG).show();
		}
		return engineOptions;
	}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

        this.keyObiectsAtlas= new BitmapTextureAtlas(this.getTextureManager(), 400, 1000, TextureOptions.DEFAULT);
        this.key1 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.keyObiectsAtlas, this, "1t.png", 0, 0, 2, 1); // 64x32
        this.key2 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.keyObiectsAtlas, this, "2t.png", 0, 200, 2, 1); // 64x32
        this.key3 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.keyObiectsAtlas, this, "3t.png", 0, 400, 2, 1); // 64x32
        this.key4 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.keyObiectsAtlas, this, "4t.png", 0, 600, 2, 1); // 64x32
        this.key5 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.keyObiectsAtlas, this, "5t.png", 0, 800, 2, 1); // 64x32
        this.keyObiectsAtlas.load();

        this.obiectsAtlas= new BitmapTextureAtlas(this.getTextureManager(), 1400, 200, TextureOptions.DEFAULT);
//        this.obj1=BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.obiectsAtlas, this,"1.png", 0, 0);
//        this.obj2=BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.obiectsAtlas, this,"2.png", 200, 0);
//        this.obj3=BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.obiectsAtlas, this,"3.png", 400, 0);
//        this.obj4=BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.obiectsAtlas, this,"4.png", 600, 0);
        this.playerMainTexture =BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.obiectsAtlas, this,"5.png", 000, 0);
    //    this.obj6=BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.obiectsAtlas, this,"6.png", 1000, 0);
        this.playerAlternativeTexture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.obiectsAtlas, this,"51.png", 200, 0);
        this.obiectsAtlas.load();

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 65, 65, TextureOptions.DEFAULT);
		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "face_box.png", 0, 0);
        this.mFaceAlternativeTextureRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this,"face_box2.png", 32, 0);
		this.mBitmapTextureAtlas.load();
		
		this.mOnScreenControlTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		this.mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_base.png", 0, 0);
		this.mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_knob.png", 128, 0);
		this.mOnScreenControlTexture.load();
		
		this.mBitmapTextureAtlas2 = new BitmapTextureAtlas(this.getTextureManager(), 72, 128, TextureOptions.DEFAULT);
		this.mBoxFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas2, this, "face_box_tiled.png", 0, 0, 2, 1); // 64x32
		this.mCircleFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas2, this, "face_circle_tiled.png", 0, 32, 2, 1); // 64x32
		this.mTriangleFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas2, this, "face_triangle_tiled.png", 0, 64, 2, 1); // 64x32
		this.mHexagonFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas2, this, "face_hexagon_tiled.png", 0, 96, 2, 1); // 64x32
		this.mBitmapTextureAtlas2.load();
	}

	public long startTime;















    Sprite face;
    Sprite face2;
    Body face_body;


    private void interpret_tmx(){
        int ix=0;
        while(ix<circlesCoords.size()){
            this.addFace(circlesCoords.get(ix).x,circlesCoords.get(ix).y ,1);
            ix++;
        }
        ix=0;
        while(ix<trianglesCoords.size()){
            this.addFace(trianglesCoords.get(ix).x,trianglesCoords.get(ix).y ,2);
            ix++;
        }
        ix=0;
        while(ix<hexagonsCoords.size()){
            this.addFace(hexagonsCoords.get(ix).x,hexagonsCoords.get(ix).y ,3);
            ix++;
        }
        ix=0;
        while(ix< crossesCoords.size()){
            this.addFace(crossesCoords.get(ix).x, crossesCoords.get(ix).y ,0);
            ix++;
        }
        ix=0;
        while(ix< starCoords.size()){
            this.addFace(starCoords.get(ix).x, starCoords.get(ix).y ,4);
            ix++;
        }


        // gracz
         face = new Sprite(playerCoords.x, playerCoords.y, this.playerMainTexture, this.getVertexBufferObjectManager());//final Sprite

        final PhysicsHandler physicsHandler = new PhysicsHandler(face);
         face_body = PhysicsFactory.createBoxBody(this.mPhysicsWorld, face, BodyType.DynamicBody, FIXTURE_DEF);//final Body
        face.registerUpdateHandler(physicsHandler);

        MassData ms=new MassData();
        ms.I=1;
        ms.mass=1f;
        face_body.setMassData(ms);
        face_body.setAngularDamping(0.1f);

        this.mScene.attachChild(face);
        face_body_connector=new PhysicsConnector(face, face_body, true, true);
        this.mPhysicsWorld.registerPhysicsConnector(face_body_connector);


        face2 = new Sprite(playerCoords.x, playerCoords.y, this.playerAlternativeTexture, this.getVertexBufferObjectManager());//final Sprite
        face2.setVisible(false);
        this.mScene.attachChild(face2);
        face2.setVisible(false);

        ///////////////////////
        this.mBoundChaseCamera.setChaseEntity(face);
    }



    PhysicsConnector face_body_connector;



    private void interpret_tmx_obiects(){

        ////////////////
        /// OBJECT LOADER
        ///////////////


        ArrayList<TMXObject> objectsTMX =this.mTMXTiledMap.getTMXObjectGroups().get(0).getTMXObjects();

        for(TMXObject object : objectsTMX){
            int cx=object.getX()-(object.getX()%32);
            int cy=object.getY()-(object.getY()%32);

            //String str="("+cx+", "+cy+")";
            //this.tekst1=this.tekst1+str;

            if(object.getName().split("_")[0].compareTo("exit")==0){
                final Rectangle blockRectangleX = new Rectangle(cx, cy, this.mTMXTiledMap.getTileWidth(), this.mTMXTiledMap.getTileHeight(), getVertexBufferObjectManager());
                blockRectangleX.setColor(1, 1, 0, 0.2f);
                this.exitFieldList.add(blockRectangleX);
                this.mScene.attachChild(blockRectangleX);
            }


            if(object.getName().split("_")[0].compareTo("gate")==0){
                final Rectangle blockRectangleX ;
                int cl=Integer.parseInt(object.getType().split("_")[2]);

                if(object.getType().split("_")[1].compareTo("up")==0){
                    blockRectangleX=new Rectangle(cx+15, cy-(32*(cl-1)), 3 , cl*32 , getVertexBufferObjectManager());
                }else
                if(object.getType().split("_")[1].compareTo("down")==0){
                    blockRectangleX=new Rectangle(cx+15, cy, 3 , cl*32 , getVertexBufferObjectManager());
                }else
                if(object.getType().split("_")[1].compareTo("left")==0){
                    blockRectangleX=new Rectangle(cx-(32*(cl-1)), cy+15, cl*32 , 3 , getVertexBufferObjectManager());
                }else
                if(object.getType().split("_")[1].compareTo("right")==0){
                    blockRectangleX=new Rectangle(cx, cy+15, cl*32 , 3 , getVertexBufferObjectManager());
                }else{
                    blockRectangleX=new Rectangle(cx, cy, this.mTMXTiledMap.getTileWidth(), this.mTMXTiledMap.getTileHeight(), getVertexBufferObjectManager());
                }

                blockRectangleX.setColor(0.1f, 0.1f, 1, 0.7f);
                final Body blockRectangle_bodyX = PhysicsFactory.createBoxBody(this.mPhysicsWorld, blockRectangleX, BodyType.StaticBody, FIXTURE_DEF);
                PhysicsConnector fx=new PhysicsConnector(blockRectangleX, blockRectangle_bodyX, true, true);

                this.gateMap.put(object.getName().split("_")[1], blockRectangleX);
                this.gateBodyMap.put(object.getName().split("_")[1], fx);
                this.gateDataMap.put(object.getName().split("_")[1], new Vector2(0,Integer.parseInt(object.getType().split("_")[0])));

                this.mScene.attachChild(blockRectangleX);
                this.mPhysicsWorld.registerPhysicsConnector(fx);

            }else if(object.getName().split("_")[0].compareTo("circle")==0){
                final Rectangle blockRectangleX = new Rectangle(cx, cy, this.mTMXTiledMap.getTileWidth(), this.mTMXTiledMap.getTileHeight(), getVertexBufferObjectManager());
                blockRectangleX.setColor(1, 0, 0, 0.2f);
                this.circleSwitchMap.put(object.getName().split("_")[1], blockRectangleX);
                this.circleSwitchConMap.put(object.getName().split("_")[1], object.getType().split("_")[1]);
                this.mScene.attachChild(blockRectangleX);
            }else if(object.getName().split("_")[0].compareTo("triangle")==0){
                final Rectangle blockRectangleX = new Rectangle(cx, cy, this.mTMXTiledMap.getTileWidth(), this.mTMXTiledMap.getTileHeight(), getVertexBufferObjectManager());
                blockRectangleX.setColor(1, 0, 0, 0.2f);
                this.triangleSwitchMap.put(object.getName().split("_")[1], blockRectangleX);
                this.triangleSwitchConMap.put(object.getName().split("_")[1], object.getType().split("_")[1]);
                this.mScene.attachChild(blockRectangleX);
            }else if(object.getName().split("_")[0].compareTo("hexagon")==0){
                final Rectangle blockRectangleX = new Rectangle(cx, cy, this.mTMXTiledMap.getTileWidth(), this.mTMXTiledMap.getTileHeight(), getVertexBufferObjectManager());
                blockRectangleX.setColor(1, 0, 0, 0.2f);
                this.hexagonSwitchMap.put(object.getName().split("_")[1], blockRectangleX);
                this.hexagonSwitchConMap.put(object.getName().split("_")[1], object.getType().split("_")[1]);
                this.mScene.attachChild(blockRectangleX);
            }else if(object.getName().split("_")[0].compareTo("box")==0){
                final Rectangle blockRectangleX = new Rectangle(cx, cy, this.mTMXTiledMap.getTileWidth(), this.mTMXTiledMap.getTileHeight(), getVertexBufferObjectManager());
                blockRectangleX.setColor(1, 0, 0, 0.2f);
                this.boxSwitchMap.put(object.getName().split("_")[1], blockRectangleX);
                this.boxSwitchConMap.put(object.getName().split("_")[1], object.getType().split("_")[1]);
                this.mScene.attachChild(blockRectangleX);

            }
        }

    }

    private void load_tmx(){
        try {
            final TMXLoader tmxLoader = new TMXLoader(this.getAssets(), this.mEngine.getTextureManager(), TextureOptions.DEFAULT, this.getVertexBufferObjectManager(), new ITMXTilePropertiesListener() {
                @Override
                public void onTMXTileWithPropertiesCreated(final TMXTiledMap pTMXTiledMap, final TMXLayer pTMXLayer, final TMXTile pTMXTile, final TMXProperties<TMXTileProperty> pTMXTileProperties) {

                    if(pTMXTileProperties.containsTMXProperty("wall", "true")) {
                        Rectangle blockRectangle1 = new Rectangle(pTMXTile.getTileX(), pTMXTile.getTileY(), pTMXTiledMap.getTileWidth(), pTMXTiledMap.getTileHeight(), PanKlocekGame.this.getVertexBufferObjectManager());
                      //  blockRectangle1.setColor(0, 1, 0, 0.5f);
                        Body blockRectangle_body1 = PhysicsFactory.createBoxBody(PanKlocekGame.this.mPhysicsWorld, blockRectangle1, BodyType.StaticBody, PanKlocekGame.FIXTURE_DEF);

                        PanKlocekGame.this.mScene.attachChild(blockRectangle1);
                        PanKlocekGame.this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(blockRectangle1, blockRectangle_body1, true, true));

                    }

                    if(pTMXTileProperties.containsTMXProperty("tri_wall", "tl")) {
                        Rectangle blockRectangle1 = new Rectangle(pTMXTile.getTileX(), pTMXTile.getTileY(), pTMXTiledMap.getTileWidth(), pTMXTiledMap.getTileHeight(), PanKlocekGame.this.getVertexBufferObjectManager());
                        Body blockRectangle_body1 =createTriangleWallBody(PanKlocekGame.this.mPhysicsWorld, blockRectangle1, BodyType.StaticBody, PanKlocekGame.FIXTURE_DEF,"tl");
                        PanKlocekGame.this.mScene.attachChild(blockRectangle1);
                        PanKlocekGame.this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(blockRectangle1, blockRectangle_body1, true, true));
                    }
                    if(pTMXTileProperties.containsTMXProperty("tri_wall", "tr")) {
                        Rectangle blockRectangle1 = new Rectangle(pTMXTile.getTileX(), pTMXTile.getTileY(), pTMXTiledMap.getTileWidth(), pTMXTiledMap.getTileHeight(), PanKlocekGame.this.getVertexBufferObjectManager());
                        Body blockRectangle_body1 =createTriangleWallBody(PanKlocekGame.this.mPhysicsWorld, blockRectangle1, BodyType.StaticBody, PanKlocekGame.FIXTURE_DEF,"tr");
                        PanKlocekGame.this.mScene.attachChild(blockRectangle1);
                        PanKlocekGame.this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(blockRectangle1, blockRectangle_body1, true, true));
                    }
                    if(pTMXTileProperties.containsTMXProperty("tri_wall", "br")) {
                        Rectangle blockRectangle1 = new Rectangle(pTMXTile.getTileX(), pTMXTile.getTileY(), pTMXTiledMap.getTileWidth(), pTMXTiledMap.getTileHeight(), PanKlocekGame.this.getVertexBufferObjectManager());
                        Body blockRectangle_body1 =createTriangleWallBody(PanKlocekGame.this.mPhysicsWorld, blockRectangle1, BodyType.StaticBody, PanKlocekGame.FIXTURE_DEF,"br");
                        PanKlocekGame.this.mScene.attachChild(blockRectangle1);
                        PanKlocekGame.this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(blockRectangle1, blockRectangle_body1, true, true));
                    }
                    if(pTMXTileProperties.containsTMXProperty("tri_wall", "bl")) {
                        Rectangle blockRectangle1 = new Rectangle(pTMXTile.getTileX(), pTMXTile.getTileY(), pTMXTiledMap.getTileWidth(), pTMXTiledMap.getTileHeight(), PanKlocekGame.this.getVertexBufferObjectManager());
                    //    blockRectangle1.setColor(0, 1, 0, 0.5f);
                     //   Body blockRectangle_body1 = PhysicsFactory.createBoxBody(PanKlocekGame.this.mPhysicsWorld, blockRectangle1, BodyType.StaticBody, PanKlocekGame.FIXTURE_DEF);
                        Body blockRectangle_body1 =createTriangleWallBody(PanKlocekGame.this.mPhysicsWorld, blockRectangle1, BodyType.StaticBody, PanKlocekGame.FIXTURE_DEF,"bl");
                        PanKlocekGame.this.mScene.attachChild(blockRectangle1);
                        PanKlocekGame.this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(blockRectangle1, blockRectangle_body1, true, true));

                    }

                    if(pTMXTileProperties.containsTMXProperty("spawn", "player")) {
                        playerCoords.set(pTMXTile.getTileX(), pTMXTile.getTileY());
                    }
                    if(pTMXTileProperties.containsTMXProperty("spawn", "triangle")) {
                        trianglesCoords.add(new Vector2(pTMXTile.getTileX(), pTMXTile.getTileY()));
                    }
                    if(pTMXTileProperties.containsTMXProperty("spawn", "circle")) {
                        circlesCoords.add(new Vector2(pTMXTile.getTileX(), pTMXTile.getTileY()));
                    }
                    if(pTMXTileProperties.containsTMXProperty("spawn", "hexagon")) {
                        hexagonsCoords.add(new Vector2(pTMXTile.getTileX(), pTMXTile.getTileY()));
                    }
                    if(pTMXTileProperties.containsTMXProperty("spawn", "cross")) {
                        crossesCoords.add(new Vector2(pTMXTile.getTileX(), pTMXTile.getTileY()));
                    }
                    if(pTMXTileProperties.containsTMXProperty("spawn", "star")) {
                        starCoords.add(new Vector2(pTMXTile.getTileX(), pTMXTile.getTileY()));
                    }
                }
            });
            this.mTMXTiledMap = tmxLoader.loadFromAsset(missionName);//missionName"tmx/mission01.tmx"





        //    this.tekst1=this.mTMXTiledMap.getTMXObjectGroups().get(0).getTMXObjects().get(2).getName();
        //    this.tekst1=this.tekst1+"  "+this.mTMXTiledMap.getTMXObjectGroups().get(0).getTMXObjects().get(2).getType();



            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(MainActivity.this, "Cactus count in this TMXTiledMap: " + MainActivity.this.mCactusCount, Toast.LENGTH_LONG).show();
                    //Toast.makeText(MainActivity.this, "wall >>> count in this TMXTiledMap: " + MainActivity.this.mWallCount, Toast.LENGTH_LONG).show();
                    //Toast.makeText(MainActivity.this, "loop? ==== count in this TMXTiledMap: " + MainActivity.this.loopCount, Toast.LENGTH_LONG).show();
                    //Toast.makeText(MainActivity.this, "tekst: " + MainActivity.this.tekst1 , Toast.LENGTH_LONG).show();

                }
            });
        } catch (final TMXLoadException e) {
            Debug.e(e);
        }

        final TMXLayer tmxLayer = this.mTMXTiledMap.getTMXLayers().get(0);
        this.mScene.attachChild(tmxLayer);


        		/* Make the camera not exceed the bounds of the TMXEntity. */
        this.mBoundChaseCamera.setBounds(0, 0, tmxLayer.getHeight(), tmxLayer.getWidth());
        this.mBoundChaseCamera.setBoundsEnabled(true);

    }

    public void setupControllers(){


        //////////////////////////////
		/* Velocity control (left). */
        final float x1 = 0;
        final float y1 = CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion.getHeight();
        final AnalogOnScreenControl velocityOnScreenControl = new AnalogOnScreenControl(x1, y1, this.mBoundChaseCamera, this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion, 0.1f, this.getVertexBufferObjectManager(), new IAnalogOnScreenControlListener() {
            @Override
            public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
                face_body.setAngularDamping(0.5f);
                if(pValueX!=0 && pValueY!=0){
                    if(face_body.getLinearVelocity().len()<70.0f){
                        face_body.applyForceToCenter(new Vector2(pValueX * 40, pValueY * 40));
                    }
                }else{
                    face_body.setLinearVelocity(face_body.getLinearVelocity().mul(0.80f));
                }
                //////////////////////////////////////////////



            }
            @Override
            public void onControlClick(final AnalogOnScreenControl pAnalogOnScreenControl) {
            }
        });
        velocityOnScreenControl.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        velocityOnScreenControl.getControlBase().setAlpha(0.5f);
        this.mScene.setChildScene(velocityOnScreenControl);




        ///////////////////////////////
		/* Rotation control (right). */
        final float y2 = (this.mPlaceOnScreenControlsAtDifferentVerticalLocations) ? 0 : y1;
        final float x2 = CAMERA_WIDTH - this.mOnScreenControlBaseTextureRegion.getWidth();
        final AnalogOnScreenControl rotationOnScreenControl = new AnalogOnScreenControl(x2, y2, this.mBoundChaseCamera, this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion, 0.1f, this.getVertexBufferObjectManager(), new IAnalogOnScreenControlListener() {
            @Override
            public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
                if(pValueX!=0 && pValueY!=0){
                    if(pValueX>0){
                        face_body.applyAngularImpulse(0.09f);
                    }else{
                        face_body.applyAngularImpulse(-0.09f);
                    }
                }else{
                }
            }
            @Override
            public void onControlClick(final AnalogOnScreenControl pAnalogOnScreenControl) {
            }
        });
        rotationOnScreenControl.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        rotationOnScreenControl.getControlBase().setAlpha(0.5f);
        velocityOnScreenControl.setChildScene(rotationOnScreenControl);

    }

    public void registerSceneHandlers(){


        //////////////////////////////

        ///handlers
        ////////////////////////



        this.mScene.registerUpdateHandler(this.mPhysicsWorld);


        this.mScene.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void reset() { }



            public void checkSwiches(Map<String, Rectangle> swx,Map<String, String> swcx,List<AnimatedSprite> spx){
                int iv;
                int jv;
                int scc=0;
                String key;
                iv=0;
                while(iv<swx.size()){
                    jv=0;
                    key=swx.keySet().toArray()[iv].toString();
                    while(jv<spx.size()){
                        if(spx.get(jv).collidesWith(swx.get(key))) {
                            PanKlocekGame.this.gateDataMap.get(swcx.get(key)).x++;
                            scc++;
                            if(scc>0) swx.get(key).setColor(0, 1, 0);
                        } else {
                            if(scc==0) swx.get(key).setColor(1, 0, 0);
                        }
                        jv++;
                    }
                    iv++;
                }
            }



            public void magn(List<AnimatedSprite> spx){

                if(magnetic) {
                    int i = 0;
                    while (i < circleBodies.size()) {
                        float a1 = face.getX() - spx.get(i).getX();
                        float a2 = (face.getY() - spx.get(i).getY());
                        float dist = (float) Math.sqrt((a1 * a1) + (a2 * a2));
                        //  Log.v("asdasdf", "test: " + dist);
                        if (dist < 600) {
                            Vector2 vec = new Vector2(((600-dist)/dist)*a1 * 0.003f, ((600-dist)/dist) * a2 * 0.003f);//130 -(dist) *
                            Vector2 vec1 = new Vector2((1f-((600-dist)/dist))*a1 * 0.003f,(1f-((600-dist)/dist)) * a2 * 0.003f);//130 -(dist) *
                            Vector2 vec2 = new Vector2(((dist)/600)*a1 * 0.003f, ((dist)/600) * a2 * 0.003f);//130 -(dist) *
                            circleBodies.get(i).setLinearVelocity(vec2.add(circleBodies.get(i).getLinearVelocity()));
//                            if(dist < 42.5f) {
//                              //  face_body.setLinearVelocity(face_body.getLinearVelocity().add((bodies.get(i).getLinearVelocity().mul(-1))));
//                              //  bodies.get(i).setLinearVelocity(bodies.get(i).getLinearVelocity().
//                            }
                        }
                        i++;
                    }

                }


                face2.setPosition(face.getX(),face.getY());
                face2.setRotation((float)Math.toDegrees(face_body.getAngle()));

            }

            public void magnet(List<AnimatedSprite> animSprites,List<Body> xBodies, int opt){

                if(magnetic) {
                    int i = 0;
                    while (i < xBodies.size()) {
                        float a1 = face.getX() - animSprites.get(i).getX();
                        float a2 = (face.getY() - animSprites.get(i).getY());
                        float dist = (float) Math.sqrt((a1 * a1) + (a2 * a2));
                        //  Log.v("asdasdf", "test: " + dist);
                        if (dist < 600) {
                            Vector2 vec = new Vector2(((600-dist)/dist)*a1 * 0.003f, ((600-dist)/dist) * a2 * 0.003f);//130 -(dist) *
                            Vector2 vec1 = new Vector2((1f-((600-dist)/dist))*a1 * 0.003f,(1f-((600-dist)/dist)) * a2 * 0.003f);//130 -(dist) *
                            Vector2 vec2 = new Vector2(((dist)/600)*a1 * 0.003f, ((dist)/600) * a2 * 0.003f);//130 -(dist) *

                            Vector2 vecX;

                                switch (opt){
                                    case 1 :
                                        vecX=vec1;
                                        break;
                                    case 2 :
                                        vecX=vec2;
                                        break;
                                    default:
                                        vecX=vec;
                                }

                            xBodies.get(i).setLinearVelocity(vecX.add(xBodies.get(i).getLinearVelocity()));
//                            if(dist < 42.5f) {
//                              //  face_body.setLinearVelocity(face_body.getLinearVelocity().add((bodies.get(i).getLinearVelocity().mul(-1))));
//                              //  bodies.get(i).setLinearVelocity(bodies.get(i).getLinearVelocity().
//                            }
                        }
                        i++;
                    }

                }


                face2.setPosition(face.getX(),face.getY());
                face2.setRotation((float)Math.toDegrees(face_body.getAngle()));

            }


            public void checkExits(){
                int iv;
                iv=0;
                while(iv<PanKlocekGame.this.exitFieldList.size()){
                    if(PanKlocekGame.this.exitFieldList.get(iv).collidesWith(face)) {
                        PanKlocekGame.this.exitFieldList.get(iv).setColor(0, 1, 0);
                        PanKlocekGame.this.StampExit();
                        PanKlocekGame.this.finish();
                    }else{
                        PanKlocekGame.this.exitFieldList.get(iv).setColor(1, 1, 0);
                    }
                    iv++;
                }

            }



            @Override
            public void onUpdate(final float pSecondsElapsed) {


                //if(blockRectangle.collidesWith(face)) {
                //	blockRectangle.setColor(0, 1, 0);
                //} else {
                //	blockRectangle.setColor(1, 0, 0);
                //}

                checkExits();
                checkSwiches(PanKlocekGame.this.circleSwitchMap,PanKlocekGame.this.circleSwitchConMap,PanKlocekGame.this.circleSprites);
                checkSwiches(PanKlocekGame.this.triangleSwitchMap,PanKlocekGame.this.triangleSwitchConMap,PanKlocekGame.this.triangleSprites);
                checkSwiches(PanKlocekGame.this.hexagonSwitchMap,PanKlocekGame.this.hexagonSwitchConMap,PanKlocekGame.this.hexagonSprites);
                checkSwiches(PanKlocekGame.this.boxSwitchMap,PanKlocekGame.this.boxSwitchConMap,PanKlocekGame.this.crossSprites);

                magn(PanKlocekGame.this.circleSprites);
                magnet(PanKlocekGame.this.circleSprites,PanKlocekGame.this.circleBodies, 1);
                magnet(PanKlocekGame.this.triangleSprites,PanKlocekGame.this.triangleBodies, 0);
                magnet(PanKlocekGame.this.crossSprites,PanKlocekGame.this.crossBodies, 0);
                magnet(PanKlocekGame.this.starSprites,PanKlocekGame.this.starBodies, 1);
                magnet(PanKlocekGame.this.hexagonSprites,PanKlocekGame.this.hexagonBodies, 1);
                /////////////////////
                //gate updates
                //////////////////////

                String key;
                int iv=0;
                while(iv<PanKlocekGame.this.gateMap.size()){
                    key=PanKlocekGame.this.gateMap.keySet().toArray()[iv].toString();
                    if(PanKlocekGame.this.gateDataMap.get(key).y-PanKlocekGame.this.gateDataMap.get(key).x<0.1f){
                        //detach
                        PanKlocekGame.this.gateMap.get(key).setVisible(false);
                        PanKlocekGame.this.gateBodyMap.get(key).getBody().setActive(false);
                    }else{
                        //attach if not
                        if(!PanKlocekGame.this.gateMap.get(key).isVisible()){
                            PanKlocekGame.this.gateMap.get(key).setVisible(true);
                            PanKlocekGame.this.gateBodyMap.get(key).getBody().setActive(true);
                        }
                    }
                    PanKlocekGame.this.gateDataMap.get(key).x=0;
                    iv++;
                }




            }
        });


        HUD hud=new HUD();//mBoundChaseCamera.getCenterX(),mBoundChaseCamera.getCenterY()
        final Rectangle rec=new Rectangle(0,0,mBoundChaseCamera.getBoundsWidth(),mBoundChaseCamera.getBoundsHeight(),getVertexBufferObjectManager());
        rec.setRed(0.5f);
        rec.setAlpha(0.2f);
        hud.attachChild(rec);
        hud.registerTouchArea(rec);

        hud.setOnAreaTouchListener(new IOnAreaTouchListener() {
            float startAngle=0;

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, ITouchArea pTouchArea, float pTouchAreaLocalX, float pTouchAreaLocalY) {
            //    Log.v("asdasdf", ">>>>test: " );

                if(pSceneTouchEvent.getAction()==TouchEvent.ACTION_DOWN){
                    magnetic=true;
                    startAngle=(float)(Math.atan2(pTouchAreaLocalY - (mBoundChaseCamera.getHeight()/2), pTouchAreaLocalX - (mBoundChaseCamera.getWidth()/2)) * 180 / Math.PI);
                    face_body_connector.setUpdateRotation(false);
                    face.setVisible(false);
                    face2.setVisible(true);

                }else if(pSceneTouchEvent.getAction()==TouchEvent.ACTION_UP){
                    magnetic=false;
                    face_body_connector.setUpdateRotation(true);
                    face.setVisible(true);
                    face2.setVisible(false);
                }

          //  Log.v("wspx","( "+pTouchAreaLocalX+", "+pTouchAreaLocalY+" )"+(mBoundChaseCamera.getWidth()/2)+" :cam: "+(mBoundChaseCamera.getHeight()/2));

           //     (mBoundChaseCamera.getCenterX()

               // face.getRotation();
               // face_body.getAngularVelocity();

                float angle = (float)(Math.atan2(pTouchAreaLocalY - (mBoundChaseCamera.getHeight()/2), pTouchAreaLocalX - (mBoundChaseCamera.getWidth()/2)) * 180 / Math.PI);
                float updateAngle=angle-startAngle;

             //   face_body.setAngularVelocity(updateAngle);

                if(magnetic) {
                   // face_body_connector.setUpdateRotation(true);
                    face_body.setTransform(face_body.getPosition().x, face_body.getPosition().y, face_body.getAngle() + (float) Math.toRadians(updateAngle));
                    face_body.setAngularVelocity(0f);
                   // face_body_connector.setUpdateRotation(false);
                }
            //    face.setRotation(face.getRotation()+updateAngle);

                startAngle=angle;

          //      face.setRotation(face.getRotation()+updateAngle);
            //    Log.v("wspx","angle: "+angle + "        face angle: "+face.getRotation()+"      face velocity:  "+face_body.getAngularVelocity()+"     body rot: "+face_body.getAngle());



                return false;
            }
        });


        mBoundChaseCamera.setHUD(hud);

    }







	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.mScene = new Scene();
		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0,0 ), true);//SensorManager.GRAVITY_EARTH

	//ime time = new Time();
	//ime.setToNow();
	
		this.startTime=System.currentTimeMillis();
		//if (startDate <= System.currentTimeMillis()  >= endDate)
		
		//System.out.println("time: " + time.hour+":"+time.minute);
		
		
		///////////////
		// TMX LOADER
		///////////////

        load_tmx();

        interpret_tmx();

      //  setupControllers();
		
	//	final float centerX = (CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
	//	final float centerY = (CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;
		
		

		///////////////////////

        interpret_tmx_obiects();
		
		
		
		/////////////????????????
		//final Rectangle blockRectangle = new Rectangle(64.0f, 64.0f, this.mTMXTiledMap.getTileWidth(), this.mTMXTiledMap.getTileHeight(), getVertexBufferObjectManager());
		//blockRectangle.setColor(1, 0, 0, 0.2f);
		//final Body blockRectangle_body = PhysicsFactory.createBoxBody(this.mPhysicsWorld, blockRectangle, BodyType.StaticBody, FIXTURE_DEF);
		//this.mScene.attachChild(blockRectangle);
		//this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(blockRectangle, blockRectangle_body, false, false));
		/////////////????????????


        registerSceneHandlers();
		
		


      //  mScene.registerTouchArea(new Rectangle(mgetVertexBufferObjectManager()));


		return this.mScene;
	}

	
	public void StampExit(){
		long mtime=System.currentTimeMillis()-this.startTime;
		long sec,msec;
		sec=mtime/1000;
		msec=(mtime%1000)/10;
		float wn=(float)sec;
		wn=wn+(0.01f*msec);
		int mn=Integer.parseInt(PanKlocekGame.missionName.split("0")[1].split(".t")[0]);
		String stamp=""+mn+"_"+wn;

	//	Toast.makeText(PanKlocekGame.this, stamp  , Toast.LENGTH_LONG).show();
    	FileOutputStream fos;
		try {
			fos = openFileOutput("stamp", Context.MODE_PRIVATE);
	   	fos.write(stamp.getBytes());
	    	fos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}

    @Override
    public void onResumeGame() {
        super.onResumeGame();

        this.enableAccelerationSensor(this);
    }

    @Override
    public void onPauseGame() {
        super.onPauseGame();

        this.disableAccelerationSensor();
    }


    boolean magnetic=false;
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
//
//
//        if(pSceneTouchEvent.getAction()==TouchEvent.ACTION_DOWN){
//            magnetic=true;
//        }else if(pSceneTouchEvent.getAction()==TouchEvent.ACTION_UP){
//            magnetic=false;
//        }
		return false;

	}




	@Override
	public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {
		
	
	}

	@Override
	public void onAccelerationChanged(AccelerationData pAccelerationData) {
        final Vector2 gravity = Vector2Pool.obtain(pAccelerationData.getX(), pAccelerationData.getY());
        face_body.setLinearVelocity(face_body.getLinearVelocity().add(gravity.mul(0.05f)));
     //   this.mPhysicsWorld.setGravity(gravity);
        Vector2Pool.recycle(gravity);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	
	public List<Body> circleBodies=new ArrayList<Body>();
    public List<Body> triangleBodies=new ArrayList<Body>();
    public List<Body> crossBodies=new ArrayList<Body>();
    public List<Body> starBodies=new ArrayList<Body>();
    public List<Body> hexagonBodies=new ArrayList<Body>();
	
	private void addFace(final float pX, final float pY,int shape) {
		this.mFaceCount++;
		Debug.d("Faces: " + this.mFaceCount);

		final AnimatedSprite faceX;
		final Body body;

		if(shape == 0) {  //CROSS
			faceX = new AnimatedSprite(pX, pY, this.key1, this.getVertexBufferObjectManager());
			body = PhysicsFactory.createBoxBody(this.mPhysicsWorld, faceX, BodyType.DynamicBody, FIXTURE_DEF);
            MassData ms=new MassData();
            ms.I=1;
            ms.mass=0.1f;
            body.setMassData(ms);
            body.setAngularDamping(0.1f);
			this.crossSprites.add(faceX);
            crossBodies.add(body);
		} else if (shape == 1) {  //CIRCLE
			faceX = new AnimatedSprite(pX, pY, this.key3, this.getVertexBufferObjectManager());
			body = PhysicsFactory.createCircleBody(this.mPhysicsWorld, faceX, BodyType.DynamicBody, FIXTURE_DEF);
            MassData ms=new MassData();
            ms.I=1;
            ms.mass=0.1f;
            body.setMassData(ms);
            body.setAngularDamping(0.1f);
			this.circleSprites.add(faceX);
            circleBodies.add(body);
		} else if (shape == 2) { //TRIANGLE
			faceX = new AnimatedSprite(pX, pY, this.key4, this.getVertexBufferObjectManager());
			body = PanKlocekGame.createTriangleBody(this.mPhysicsWorld, faceX, BodyType.DynamicBody, FIXTURE_DEF);
            MassData ms=new MassData();
            ms.I=1;
            ms.mass=0.1f;
            body.setMassData(ms);
            body.setAngularDamping(0.1f);
			this.triangleSprites.add(faceX);
            triangleBodies.add(body);
		} else if (shape == 3) {  //HEXAGON
			faceX = new AnimatedSprite(pX, pY, this.key2, this.getVertexBufferObjectManager());
			body = PanKlocekGame.createHexagonBody(this.mPhysicsWorld, faceX, BodyType.DynamicBody, FIXTURE_DEF);
            MassData ms=new MassData();
            ms.I=1;
            ms.mass=0.1f;
            body.setMassData(ms);
            body.setAngularDamping(0.1f);
			this.hexagonSprites.add(faceX);
            hexagonBodies.add(body);
			
		}else  { //if (shape == 4)         //STAR

            faceX = new AnimatedSprite(pX, pY, this.key5, this.getVertexBufferObjectManager());
            body = PanKlocekGame.createStarBody(this.mPhysicsWorld, faceX, BodyType.DynamicBody, FIXTURE_DEF);
            MassData ms=new MassData();
            ms.I=1;
            ms.mass=0.1f;
            body.setMassData(ms);
            this.starSprites.add(faceX);
            starBodies.add(body);
        }

		faceX.animate(200);

		this.mScene.attachChild(faceX);
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(faceX, body, true, true));
	}



    private static Body createStarBody(final PhysicsWorld pPhysicsWorld, final IAreaShape pAreaShape, final BodyType pBodyType, final FixtureDef pFixtureDef) {
		/* Remember that the vertices are relative to the center-coordinates of the Shape. */
        final float halfWidth = pAreaShape.getWidthScaled() * 0.5f / PIXEL_TO_METER_RATIO_DEFAULT;
        final float halfHeight = pAreaShape.getHeightScaled() * 0.5f / PIXEL_TO_METER_RATIO_DEFAULT;

        final Vector2[] starVertices = {
                new Vector2(0.0f*halfWidth,	-0.99f*halfHeight),
                new Vector2(0.24f*halfWidth,	-0.27f*halfHeight),
                new Vector2(0.98f*halfWidth,	-0.24f*halfHeight),
                new Vector2(0.44f*halfWidth,	0.24f*halfHeight),
                new Vector2(0.61f*halfWidth,	0.99f*halfHeight),
                new Vector2(0.0f*halfWidth,  0.55f*halfHeight),
                new Vector2(-0.62f*halfWidth,	0.99f*halfHeight),
                new Vector2(-0.41f*halfWidth,	0.25f*halfHeight),
//                new Vector2(-0.99f*halfWidth,	-0.24f*halfHeight),
 //               new Vector2(-0.25f*halfWidth,	-0.27f*halfHeight)
        };

        return PhysicsFactory.createPolygonBody(pPhysicsWorld, pAreaShape, starVertices, pBodyType, pFixtureDef);
    }
	/**
	 * Creates a {@link Body} based on a {@link PolygonShape} in the form of a triangle:
	 * <pre>
	 *  /\
	 * /__\
	 * </pre>
	 */
	private static Body createTriangleBody(final PhysicsWorld pPhysicsWorld, final IAreaShape pAreaShape, final BodyType pBodyType, final FixtureDef pFixtureDef) {
		/* Remember that the vertices are relative to the center-coordinates of the Shape. */
		final float halfWidth = pAreaShape.getWidthScaled() * 0.5f / PIXEL_TO_METER_RATIO_DEFAULT;
		final float halfHeight = pAreaShape.getHeightScaled() * 0.5f / PIXEL_TO_METER_RATIO_DEFAULT;

//		final float top = -halfHeight;
//		final float bottom = halfHeight;
//		final float left = -halfHeight;
//		final float centerX = 0;
//		final float right = halfWidth;
//
//		final Vector2[] vertices = {
//				new Vector2(centerX, top),
//				new Vector2(right, bottom),
//				new Vector2(left, bottom)
//		};




        final Vector2[] vertices2 = {
                new Vector2(0.98f*halfWidth,	-0.99f*halfHeight),
                new Vector2(0.47f*halfWidth,	0.99f*halfHeight),
                new Vector2(-0.99f*halfWidth,	-0.46f*halfHeight)
        };
		return PhysicsFactory.createPolygonBody(pPhysicsWorld, pAreaShape, vertices2, pBodyType, pFixtureDef);
	}



    private static Body createTriangleWallBody(final PhysicsWorld pPhysicsWorld, final IAreaShape pAreaShape, final BodyType pBodyType, final FixtureDef pFixtureDef,String opt) {
		/* Remember that the vertices are relative to the center-coordinates of the Shape. */
        final float halfWidth = pAreaShape.getWidthScaled() * 0.5f / PIXEL_TO_METER_RATIO_DEFAULT;
        final float halfHeight = pAreaShape.getHeightScaled() * 0.5f / PIXEL_TO_METER_RATIO_DEFAULT;

        Body out;
        if(opt.compareTo("tl")==0) {
            final Vector2[] vertices2 = {
                    new Vector2(-1f * halfWidth, -1f * halfHeight),
                    new Vector2(1f * halfWidth, -1f * halfHeight),
                    new Vector2(-1 * halfWidth, 1f * halfHeight)
            };
            out=PhysicsFactory.createPolygonBody(pPhysicsWorld, pAreaShape, vertices2, pBodyType, pFixtureDef);
        }else   if(opt.compareTo("tr")==0) {
                final Vector2[] vertices2 = {
                        new Vector2(-1f * halfWidth, -1f * halfHeight),
                        new Vector2(1f * halfWidth, -1f * halfHeight),
                        new Vector2(1f * halfWidth, 1f * halfHeight)
            };
            out=PhysicsFactory.createPolygonBody(pPhysicsWorld, pAreaShape, vertices2, pBodyType, pFixtureDef);
        }else   if(opt.compareTo("br")==0) {
            final Vector2[] vertices2 = {
                    new Vector2(1f * halfWidth, -1f * halfHeight),
                    new Vector2(1f * halfWidth, 1f * halfHeight),
                    new Vector2(-1f * halfWidth, 1f * halfHeight)
            };
            out=PhysicsFactory.createPolygonBody(pPhysicsWorld, pAreaShape, vertices2, pBodyType, pFixtureDef);
        }else { //bl
            final Vector2[] vertices2 = {
                    new Vector2(1f * halfWidth, 1f * halfHeight),
                    new Vector2(-1f * halfWidth, 1f * halfHeight),
                    new Vector2(-1f * halfWidth, -1f * halfHeight)
            };
            out=PhysicsFactory.createPolygonBody(pPhysicsWorld, pAreaShape, vertices2, pBodyType, pFixtureDef);
        }
        return out;
    }
	/**
	 * Creates a {@link Body} based on a {@link PolygonShape} in the form of a hexagon:
	 * <pre>
	 *  /\
	 * /  \
	 * |  |
	 * |  |
	 * \  /
	 *  \/
	 * </pre>
	 */
	private static Body createHexagonBody(final PhysicsWorld pPhysicsWorld, final IAreaShape pAreaShape, final BodyType pBodyType, final FixtureDef pFixtureDef) {
		/* Remember that the vertices are relative to the center-coordinates of the Shape. */
		final float halfWidth = pAreaShape.getWidthScaled() * 0.5f / PIXEL_TO_METER_RATIO_DEFAULT;
		final float halfHeight = pAreaShape.getHeightScaled() * 0.5f / PIXEL_TO_METER_RATIO_DEFAULT;

		/* The top and bottom vertex of the hexagon are on the bottom and top of hexagon-sprite. */
		final float top = -halfHeight;
		final float bottom = halfHeight;

		final float centerX = 0;

		/* The left and right vertices of the heaxgon are not on the edge of the hexagon-sprite, so we need to inset them a little. */
		final float left = -halfWidth + 2.5f / PIXEL_TO_METER_RATIO_DEFAULT;
		final float right = halfWidth - 2.5f / PIXEL_TO_METER_RATIO_DEFAULT;
		final float higher = top + 8.25f / PIXEL_TO_METER_RATIO_DEFAULT;
		final float lower = bottom - 8.25f / PIXEL_TO_METER_RATIO_DEFAULT;

		final Vector2[] vertices = {
				new Vector2(centerX, top),
				new Vector2(right, higher),
				new Vector2(right, lower),
				new Vector2(centerX, bottom),
				new Vector2(left, lower),
				new Vector2(left, higher)
		};

		return PhysicsFactory.createPolygonBody(pPhysicsWorld, pAreaShape, vertices, pBodyType, pFixtureDef);
	}
	
	
	
	
	

	
	
	
}

