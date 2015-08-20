using UnityEngine;
using System.Collections;

public class Rotate : MonoBehaviour
{
	public Vector3 rotation;
	public Vector3 rotationOnTouch;

	void FixedUpdate ( )
	{
		transform.Rotate( rotation );
		transform.Rotate( rotationOnTouch * Input.touchCount );
	}
}
