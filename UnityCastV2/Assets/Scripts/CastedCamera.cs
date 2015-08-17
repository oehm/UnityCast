using UnityEngine;
using System.Collections;
using System;

[RequireComponent(typeof(Camera))]
public class CastedCamera : MonoBehaviour
{

	//public Material debugMaterial;

	// Use this for initialization
	void Start ( )
	{
		Camera cam = GetComponent<Camera>( );

		//if ( Display.displays.Length != 2)
		//{
		//	Display tmpMainDisplay = Display.displays[0];


		//	Display.displays = new Display[]
		//	{
		//		tmpMainDisplay,
		//		new Display()
		//	};

		//	debugMaterial.color = Color.red;
		//}

		if ( Display.displays.Length > 1 )
		{
			Display castDisplay = Display.displays[1];

			castDisplay.SetRenderingResolution( 512, 512 );
			cam.SetTargetBuffers( castDisplay.colorBuffer, castDisplay.depthBuffer );
		}
		cam.enabled = Display.displays.Length > 1;
	}

}
