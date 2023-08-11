import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PropertyTypeDetailComponent } from './property-type-detail.component';

describe('PropertyType Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PropertyTypeDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PropertyTypeDetailComponent,
              resolve: { propertyType: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(PropertyTypeDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load propertyType on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PropertyTypeDetailComponent);

      // THEN
      expect(instance.propertyType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
