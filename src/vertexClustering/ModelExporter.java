package vertexClustering;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;

public class ModelExporter {
	
	public ModelExporter() {}
	
	// Function used to generate OBJ files of a compressed version of a model
	// It will create one for each level in the range [highLevel, lowLevel], both included
	public void exportModelsFromLevels(RawModel model, Integer highLevel, Integer lowLevel) {
		Octree modelOctree = new Octree(model);
		modelOctree.generateOctreeLevels(lowLevel);
		
		for (int i = highLevel; i <= lowLevel; i++) {
			OctreeInfo compressedData = modelOctree.getDataFromOctreeLevel(i);
			generateOBJFile(model.getModelName(), i, compressedData);
		}
	}
	
	// Generates an OBJ file given the information of a compressed model
	public void generateOBJFile(String modelName, Integer compLevel, OctreeInfo compData) {
		ArrayList<Vector3f> newVer = compData.getNewVertices();
		ArrayList<Vector2f> newTex = compData.getNewTextures();
		ArrayList<Vector3f> newNor = compData.getNewNormals();
		ArrayList<Integer> newInd = compData.getNewIndices();
		
		FileWriter fw = null;
		try {
			fw = new FileWriter("res/" + modelName + "CompressionLvl" + compLevel + ".obj");
			fw.write("# Compressed model generated by Rafael �vila\n");
			
			// write the vertex positions
			for (int i = 0; i < newVer.size(); i++) {
				// Format used to write 6 decimal positions and to avoid scientific notation
				String xCoord = String.format(Locale.ROOT, "%.6f", newVer.get(i).x);
				String yCoord = String.format(Locale.ROOT, "%.6f", newVer.get(i).y);
				String zCoord = String.format(Locale.ROOT, "%.6f", newVer.get(i).z);
				fw.write("v " + xCoord + " " + yCoord + " " + zCoord + "\n");
			}
			
			// write the texture coordintes
			for (int i = 0; i < newTex.size(); i++) {
				String xText = String.format(Locale.ROOT, "%.6f", newTex.get(i).x);
				String yText = String.format(Locale.ROOT, "%.6f", newTex.get(i).y);
				fw.write("vt " + xText + " " + yText + "\n");
			}
			
			// write the vertex normals
			for (int i = 0; i < newNor.size(); i++) {
				String xNorm = String.format(Locale.ROOT, "%.6f", newNor.get(i).x);
				String yNorm = String.format(Locale.ROOT, "%.6f", newNor.get(i).y);
				String zNorm = String.format(Locale.ROOT, "%.6f", newNor.get(i).z);
				fw.write("vn " + xNorm + " " + yNorm + " " + zNorm + "\n");
			}
			
			// write the faces (vertex indices)
			for (int i = 0; i < newInd.size(); i+=3) {
				// Since the information of the original model was ordered when first loaded
				// the indices for the vertex pos, tex coords and normals are the same for each vertex
				// Example of a face: 5/5/5 7/7/7 3/3/3
				fw.write("f " + newInd.get(i) + "/" + newInd.get(i) + "/" + newInd.get(i) + " "
						+ newInd.get(i+1) + "/" + newInd.get(i+1) + "/" + newInd.get(i+1) + " "
						+ newInd.get(i+2) + "/" + newInd.get(i+2) + "/" + newInd.get(i+2) + "\n");
			}
			
			fw.close();
		} catch (IOException e) {
			System.err.println("Error writting the file");
			System.exit(-1);
		}
		
	}

}
