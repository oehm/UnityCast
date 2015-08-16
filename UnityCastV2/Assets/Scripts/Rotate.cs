using UnityEngine;
using System.Collections;

public class Rotate : MonoBehaviour
{
	public Vector3 rotation;

	void FixedUpdate ( )
	{
		transform.Rotate( rotation );
	}
}
