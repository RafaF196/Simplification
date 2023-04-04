package main;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRender.TextMaster;
import models.RawModel;
import models.TexturedModel;
import objFileLoader.ObjFileLoader;
import render.DisplayManager;
import render.Loader;
import render.MasterRenderer;
import textures.ModelTexture;
import vertexClustering.ModelExporter;

public class MainTest {

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		MasterRenderer renderer = new MasterRenderer(loader);
		TextMaster.init(loader);
		
		Camera camera = new Camera();
		List<Entity> entities = new ArrayList<Entity>();
		
		entities.add(new Entity(loadModel("tea", "tea", loader), new Vector3f(0, -1, 0), 0, 0.25f));
		entities.add(new Entity(loadModel("teaCompressionLvl6", "tea", loader), new Vector3f(0, -1, 0), 0, 0.25f));
		entities.add(new Entity(loadModel("teaCompressionLvl5", "tea", loader), new Vector3f(0, -1, 0), 0, 0.25f));
		entities.add(new Entity(loadModel("teaCompressionLvl4", "tea", loader), new Vector3f(0, -1, 0), 0, 0.25f));
		entities.add(new Entity(loadModel("teaCompressionLvl3", "tea", loader), new Vector3f(0, -1, 0), 0, 0.25f));
		
		entities.add(new Entity(loadModel("meta", "meta", loader), new Vector3f(0, -1, 0), 0, 0.4f));
		entities.add(new Entity(loadModel("metaCompressionLvl6", "meta", loader), new Vector3f(0, -1, 0), 0, 0.4f));
		entities.add(new Entity(loadModel("metaCompressionLvl5", "meta", loader), new Vector3f(0, -1, 0), 0, 0.4f));
		entities.add(new Entity(loadModel("metaCompressionLvl4", "meta", loader), new Vector3f(0, -1, 0), 0, 0.4f));
		entities.add(new Entity(loadModel("metaCompressionLvl3", "meta", loader), new Vector3f(0, -1, 0), 0, 0.4f));
		
		entities.add(new Entity(loadModel("dragon", "dragon", loader), new Vector3f(0, -1, 0), 0, 0.25f));
		entities.add(new Entity(loadModel("dragonCompressionLvl6", "dragon", loader), new Vector3f(0, -1, 0), 0, 0.25f));
		entities.add(new Entity(loadModel("dragonCompressionLvl5", "dragon", loader), new Vector3f(0, -1, 0), 0, 0.25f));
		entities.add(new Entity(loadModel("dragonCompressionLvl4", "dragon", loader), new Vector3f(0, -1, 0), 0, 0.25f));
		entities.add(new Entity(loadModel("dragonCompressionLvl3", "dragon", loader), new Vector3f(0, -1, 0), 0, 0.25f));
		
		FontType font = new FontType(loader.loadTexture("candara"), new File("res/candara.fnt"));
		GUIText fps_text = new GUIText("FPS: ", 1.8f, font, new Vector2f(0.008f, 0.008f), 1f, false);
		GUIText compression_text = new GUIText("Compression level: None", 1.6f, font, new Vector2f(0.4f, 0.008f), 1f, false);
		fps_text.setColour(1, 0, 0);
		compression_text.setColour(1, 0, 0);
		
		Integer entityToRender, entityToShow = 0, entityCompression = 0, N = 1;
		Integer counter = 1;
		float delta, deltacount = 0;
		
		String[] compLv = new String[5];
		compLv[0] = "None"; compLv[1] = "6"; compLv[2] = "5"; compLv[3] = "4"; compLv[4] = "3";
		
		Boolean objectChangePressed = false, compressionChangePressed = false, exportPressed = false,
				NchangePressed = false;

		ModelExporter testExp = new ModelExporter();
		
		while(!Display.isCloseRequested()) {

			// Change object
			if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
				if (!objectChangePressed) {
					entityToShow = (entityToShow + 5)%(3*5);
					objectChangePressed = true;
				}
			} else {
				objectChangePressed = false;
			}
			
			// Change object
			if (Keyboard.isKeyDown(Keyboard.KEY_V)) {
				if (!compressionChangePressed) {
					entityCompression = (entityCompression + 1)%5;
					compression_text.remove();
					compression_text = new GUIText("Compression level: " + compLv[entityCompression], 1.6f,
							font, new Vector2f(0.4f, 0.008f), 1f, false);
					compression_text.setColour(1, 0, 0);
					compressionChangePressed = true;
				}
			} else {
				compressionChangePressed = false;
			}
			
			// Export compressed models
			if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
				if (!exportPressed) {
					testExp.exportModelsFromLevels(entities.get(entityToShow).getModel().getRawModel(), 3, 6);
					exportPressed = true;
				}
			} else {
				exportPressed = false;
			}
			
			// Change N
			if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
				if (!NchangePressed) {
					if (N < 15) N++;
					NchangePressed = true;
				}
			} else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
				if (!NchangePressed) {
					if (N > 1) N--;
					NchangePressed = true;
				}
			} else {
				NchangePressed = false;
			}
			
			entityToRender = entityToShow + entityCompression;
			
			delta = DisplayManager.getFrameTimeSeconds();
			
	        if (deltacount > 0.2f) { // update every 200 ms or so
	        	float fps = (float) (counter/deltacount);
		        DecimalFormat decimalFormat = new DecimalFormat("00");
		        String numberAsString = decimalFormat.format(fps);
		        fps_text.remove();
		        fps_text = new GUIText("FPS: " + numberAsString, 1.8f, font, new Vector2f(0.008f, 0.008f), 1f, false);
		        fps_text.setColour(1, 0, 0);
				counter = 1;
				deltacount = 0;
	        } else {
	        	counter++;
	        	deltacount += delta;
	        }
			
			camera.move();
			renderer.renderScene(entities.subList(entityToRender, entityToRender+1), camera, N);
			TextMaster.render();
			DisplayManager.updateDisplay();
			
		}
		
		TextMaster.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}
	
	private static TexturedModel loadModel(String modelName, String textureName, Loader loader){
		RawModel model = ObjFileLoader.loadOBJ(modelName, loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture(textureName));
		return new TexturedModel(model, texture);
	}

}
