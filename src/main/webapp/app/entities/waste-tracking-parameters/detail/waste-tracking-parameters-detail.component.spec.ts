import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { WasteTrackingParametersDetailComponent } from './waste-tracking-parameters-detail.component';

describe('WasteTrackingParameters Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WasteTrackingParametersDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: WasteTrackingParametersDetailComponent,
              resolve: { wasteTrackingParameters: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(WasteTrackingParametersDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load wasteTrackingParameters on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', WasteTrackingParametersDetailComponent);

      // THEN
      expect(instance.wasteTrackingParameters).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
